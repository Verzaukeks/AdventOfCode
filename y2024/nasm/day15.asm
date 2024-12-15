%define SIZE 50

section .data
    INPUT:  incbin "../inputs/d15"
            db 0
    FMT:    db "%lld", 10, 0

section .bss
    INPUT2: resb 2 * SIZE * (SIZE + 2) + 1
    TILE:   resw 256

section .text
    global main
    extern printf
    default rel


main:
    sub rsp, 40

    call expand

    call a1
    lea rcx, [FMT]
    mov rdx, rax
    call printf

    call a2
    lea rcx, [FMT]
    mov rdx, rax
    call printf

    ; lea rcx, [INPUT2]
    ; call printf

    add rsp, 40
    xor rax, rax
    ret


expand:
    mov word [TILE + 2 * '#'], "##"
    mov word [TILE + 2 * 'O'], "[]"
    mov word [TILE + 2 * '.'], ".."
    mov word [TILE + 2 * '@'], "@."
    mov word [TILE + 2 * 13], 13 + 13 * 256 ; '\r\r'
    mov word [TILE + 2 * 10], 13 + 10 * 256 ; '\r\n'

    xor rax, rax
    xor rbx, rbx
    lea r8, [INPUT]
    lea r9, [INPUT2]
    lea r10, [TILE]
.fill:
    mov al, [r8]
    mov bx, [r10 + 2 * rax]
    cmp bx, 0
    jz .end
    mov [r9], bx
    add r8, 1
    add r9, 2
    jmp .fill
.end:
    ret


a1:
    xor rax, rax
    lea r8, [INPUT]
    lea r9, [INPUT + SIZE * (SIZE + 2) + 2]
.find_robot:
    add r8, 1
    mov al, [r8]
    cmp al, '@'
    jne .find_robot
.next_instruction:
    mov al, [r9]
    add r9, 1
    cmp al, 0
    jz .calc

    mov r10, r8

    mov r11, 1
    cmp al, '>'
    je .move

    mov r11, -1
    cmp al, '<'
    je .move

    mov r11, SIZE + 2
    cmp al, 'v'
    je .move

    mov r11, -SIZE - 2
    cmp al, '^'
    je .move

    jmp .next_instruction
.move:
    add r10, r11
    mov al, [r10]
    cmp al, '#'
    je .next_instruction
    cmp al, '.'
    jne .move
.fill:
    cmp byte [r10], '@'
    je .fill_end

    mov byte [r10], 'O'
    sub r10, r11
    jmp .fill
.fill_end:
    mov byte [r10], '.'
    mov byte [r10 + r11], '@'
    add r8, r11
    jmp .next_instruction
.calc:
    xor r8, r8
    lea r9, [INPUT]
    xor r10, r10
    xor r11, r11
.calc_step:
    mov al, [r9]
    cmp al, 0
    jz .end

    cmp al, 10
    je .calc_newline

    cmp al, 'O'
    jne .calc_next

    mov rbx, r11
    imul rbx, 100
    add rbx, r10
    add r8, rbx
.calc_next:
    add r9, 1
    add r10, 1
    jmp .calc_step
.calc_newline:
    add r9, 1
    xor r10, r10
    add r11, 1
    jmp .calc_step
.end:
    mov rax, r8
    ret


a2:
    xor rax, rax
    lea r8, [INPUT2]
    lea r9, [INPUT + SIZE * (SIZE + 2) + 2]
.find_robot:
    add r8, 1
    mov al, [r8]
    cmp al, '@'
    jne .find_robot
.next_instruction:
    mov al, [r9]
    add r9, 1
    cmp al, 0
    jz .calc

    mov r10, r8

    mov r11, 1
    cmp al, '>'
    je .move_lr

    mov r11, -1
    cmp al, '<'
    je .move_lr

    mov r11, 2 * (SIZE + 2)
    cmp al, 'v'
    je .move_ud

    mov r11, -2 * (SIZE + 2)
    cmp al, '^'
    je .move_ud

    jmp .next_instruction
.move_lr:
    add r10, r11
    mov al, [r10]
    cmp al, '#'
    je .next_instruction
    cmp al, '.'
    jne .move_lr
    neg r11
.fill:
    cmp byte [r10], '@'
    je .fill_end

    mov al, [r10 + r11]
    mov byte [r10], al
    add r10, r11
    jmp .fill
.fill_end:
    mov byte [r10], '.'
    sub r8, r11
    jmp .next_instruction
.move_ud:
    push r8
    call move_test
    pop r8
    cmp rax, 0
    jz .next_instruction
    push r8
    call move
    pop r8
    add r8, r11
    jmp .next_instruction
.calc:
    xor r8, r8
    lea r9, [INPUT2]
    xor r10, r10
    xor r11, r11
.calc_step:
    mov al, [r9]
    cmp al, 0
    jz .end

    cmp al, 10
    je .calc_newline

    cmp al, '['
    jne .calc_next

    mov rbx, r11
    imul rbx, 100
    add rbx, r10
    add r8, rbx
.calc_next:
    add r9, 1
    add r10, 1
    jmp .calc_step
.calc_newline:
    add r9, 1
    xor r10, r10
    add r11, 1
    jmp .calc_step
.end:
    mov rax, r8
    ret


move_test:
    xor rax, rax
    mov rbx, [rsp + 8]
    mov al, [rbx]
    cmp al, '.'
    je .success
    cmp al, '#'
    je .fail
    cmp al, '@'
    je .robot
    cmp al, '['
    je .leftb
    cmp al, ']'
    je .rightb
    jmp .fail
.robot:
    add rbx, r11
    push rbx
    call move_test
    pop rbx
    ret
.leftb:
    add rbx, 1
    mov [rsp + 8], rbx
.rightb:
    add rbx, r11
    push rbx
    call move_test
    pop rbx
    cmp rax, 0
    jz .fail
    mov rbx, [rsp + 8]
    sub rbx, 1
    add rbx, r11
    push rbx
    call move_test
    pop rbx
    ret
.success:
    mov rax, 1
    ret
.fail:
    xor rax, rax
    ret


move:
    xor rax, rax
    mov rbx, [rsp + 8]
    mov al, [rbx]
    cmp al, '@'
    je .robot
    cmp al, '['
    je .leftb
    cmp al, ']'
    je .rightb
    ret
.robot:
    add rbx, r11
    push rbx
    call move
    pop rbx
    mov rbx, [rsp + 8]
    mov byte [rbx], '.'
    mov byte [rbx + r11], '@'
    ret
.leftb:
    add rbx, 1
    mov [rsp + 8], rbx
.rightb:
    add rbx, r11
    push rbx
    call move
    pop rbx
    mov rbx, [rsp + 8]
    sub rbx, 1
    add rbx, r11
    push rbx
    call move
    pop rbx
    mov rbx, [rsp + 8]
    mov byte [rbx - 1], '.'
    mov byte [rbx    ], '.'
    mov byte [rbx + r11 - 1], '['
    mov byte [rbx + r11    ], ']'
.end:
    ret