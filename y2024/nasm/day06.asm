%define SIZE 130

section .data
    INPUT:  incbin "../inputs/d06"
    INPUT2: incbin "../inputs/d06"
            db 0
    FMT:    db "%d", 10, 0

section .text
    global main
    extern printf
    default rel


main:
    sub rsp, 40

    call a1
    lea rcx, [FMT]
    mov rdx, rax
    call printf

    call a2
    lea rcx, [FMT]
    mov rdx, rax
    call printf

    add rsp, 40
    xor rax, rax
    ret


%macro get_char 1
    lea rax, [%1]
    mov rbx, r9
    imul rbx, SIZE + 2 ; \r\n
    add rbx, rax
    add rbx, r8
    xor rax, rax
    mov al, [rbx]
%endmacro


; r8: x coord
; r9: y coord
; r10: result
; r11: direction x
; r12: direction y
a1:
    mov r11, 0
    mov r12, -1
    call find_guard
    push r8
    push r9
.simulate:
    get_char INPUT
    mov byte [rbx], 'X'
    add r8, r11
    add r9, r12
    cmp r8, 0
    jl .calc
    cmp r8, SIZE
    jge .calc
    cmp r9, 0
    jl .calc
    cmp r9, SIZE
    jge .calc
    get_char INPUT
    cmp al, '#'
    jne .simulate
    sub r8, r11
    sub r9, r12
    xor r11, r12
    xor r12, r11
    xor r11, r12
    neg r11
    jmp .simulate
.calc:
    xor r10, r10
    mov r8, 0
    mov r9, 0
.calc_step:
    get_char INPUT
    cmp al, 'X'
    jne .calc_next
    add r10, 1
.calc_next:
    add r8, 1
    cmp r8, SIZE
    jl .calc_step
    xor r8, r8
    add r9, 1
    cmp r9, SIZE
    jl .calc_step
.end:
    pop r9
    pop r8
    get_char INPUT
    mov byte [rbx], '^'
    mov rax, r10
    ret


; r8: x coord
; r9: y coord
find_guard:
    xor r8, r8
    xor r9, r9
.check:
    get_char INPUT
    cmp al, '^'
    je .end
    add r8, 1
    cmp r8, SIZE
    jl .check
    xor r8, r8
    add r9, 1
    cmp r9, SIZE
    jl .check
.end:
    ret


; r8: x coord
; r9: y coord
; r10: result
; r11: direction x
; r12: direction y
a2:
    xor r10, r10
    call find_guard
    push r8
    push r9
    mov r8, 0
    mov r9, 0
.next:
    get_char INPUT
    cmp al, 'X'
    je .test
.next_step:
    add r8, 1
    cmp r8, SIZE
    jl .next
    xor r8, r8
    add r9, 1
    cmp r9, SIZE
    jl .next
    jmp .end
.test:
    get_char INPUT2
    mov byte [rbx], '#'
    push r8
    push r9
    mov r8, [rsp + 24]
    mov r9, [rsp + 16]
    mov r11, 0
    mov r12, -1
.simulate:
    get_char INPUT2
    cmp al, '.' + 4
    je .test_success
    add al, 1
    mov byte [rbx], al
    add r8, r11
    add r9, r12
    cmp r8, 0
    jl .test_end
    cmp r8, SIZE
    jge .test_end
    cmp r9, 0
    jl .test_end
    cmp r9, SIZE
    jge .test_end
    get_char INPUT2
    cmp al, '#'
    jne .simulate
    sub r8, r11
    sub r9, r12
    xor r11, r12
    xor r12, r11
    xor r11, r12
    neg r11
    jmp .simulate
.test_success:
    add r10, 1
.test_end:
    call reset
    pop r9
    pop r8
    get_char INPUT2
    mov byte [rbx], '.'
    jmp .next_step
.end:
    pop r9
    pop r8
    mov rax, r10
    ret


reset:
    xor rax, rax
    lea r8, [INPUT2]
.next:
    mov al, [r8]
    add r8, 1
    cmp al, 0
    jz .end
    cmp al, '#'
    je .next
    mov byte [r8 - 1], '.'
    jmp .next
.end:
    ret
