
section .data
    INPUT:  incbin "../inputs/d22"
            db 0
    FMT:    db "%lld", 10, 0

section .bss
    ns:     resq 4096
    seq:    resq 1 << 20
    vis:    resq 1 << 20

section .text
    global main
    extern printf
    default rel


main:
    sub rsp, 32

    call parse

    call a1
    lea rcx, [FMT]
    mov rdx, rax
    xor rax, rax
    call printf

    call a2
    lea rcx, [FMT]
    mov rdx, rax
    xor rax, rax
    call printf

    add rsp, 32
    xor rax, rax
    ret


; rax: current char
; rbx: current number
; r8: input pointer
; r9: nums pointer
parse:
    xor rax, rax
    xor rbx, rbx
    lea r8, [INPUT]
    lea r9, [ns]
.empty:
    mov al, [r8]
    add r8, 1
    cmp rax, 0
    jz .end
    cmp rax, '0'
    jl .empty
    cmp rax, '9'
    jg .empty
    sub r8, 1
.number:
    mov al, [r8]
    add r8, 1
    cmp rax, '0'
    jl .add
    cmp rax, '9'
    jg .add
    sub rax, '0'
    imul rbx, 10
    add rbx, rax
    jmp .number
.add:
    mov [r9], rbx
    add r9, 8
    xor rbx, rbx
    jmp .empty
.end:
    mov qword [r9], -1
    ret


a1:
    lea rsi, [ns]
    xor r8, r8
.next:
    mov rax, [rsi]
    cmp rax, -1
    je .end

    add rsi, 8
    mov rdi, 2000
.step:
    mov rbx, rax
    shl rbx, 6
    xor rax, rbx
    and rax, 0xFFFFFF

    mov rbx, rax
    shr rbx, 5
    xor rax, rbx
    and rax, 0xFFFFFF

    mov rbx, rax
    shl rbx, 11
    xor rax, rbx
    and rax, 0xFFFFFF

    sub rdi, 1
    cmp rdi, 0
    jne .step

    add r8, rax
    jmp .next
.end:
    mov rax, r8
    ret


a2:
    lea rsi, [ns]
    lea r14, [seq]
    lea r15, [vis]
.next:
    mov rax, [rsi]
    cmp rax, -1
    je .find

    add rsi, 8
    xor rdi, rdi
    xor r10, r10
    xor r11, r11
.step:
    mov rbx, rax
    shl rbx, 6
    xor rax, rbx
    and rax, 0xFFFFFF

    mov rbx, rax
    shr rbx, 5
    xor rax, rbx
    and rax, 0xFFFFFF

    mov rbx, rax
    shl rbx, 11
    xor rax, rbx
    and rax, 0xFFFFFF

    push rax
    xor rdx, rdx
    mov rcx, 10
    idiv rcx
    pop rax

    mov rcx, rdx
    sub rcx, r10
    and rcx, 0b11111

    mov r10, rdx

    shl r11, 5
    add r11, rcx
    and r11, 0xFFFFF

    cmp rdi, 4
    jl .step_end

    cmp [r15 + r11 * 8], rsi
    je .step_end
    mov [r15 + r11 * 8], rsi
    add [r14 + r11 * 8], r10
.step_end:
    add rdi, 1
    cmp rdi, 2000
    jl .step
    jmp .next
.find:
    xor rax, rax
    mov rsi, -1
.find_next:
    add rsi, 1
    cmp rsi, 1 << 20
    je .end

    mov rbx, [r14 + rsi * 8]
    cmp rbx, rax
    jle .find_next
    mov rax, rbx
    jmp .find_next
.end:
    ret
