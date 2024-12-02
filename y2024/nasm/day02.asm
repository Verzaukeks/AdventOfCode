; why am i even doing this...

section .data
    INPUT:  incbin "../inputs/d02"
    FMT:    db "%d", 10, 0

section .bss
    ns:     resd 10000

section .text
    global main
    extern printf


main:
    push rbp

    call parse

    call a1
    lea rcx, [rel FMT]
    mov rdx, rax
    call printf
    
    pop rbp
    xor rax, rax
    ret


; rax: current char
; rbx: current number
; r8: input pointer
; r9: nums pointer
parse:
    mov rbx, 0
    mov r8, INPUT
    mov r9, ns
    mov r10, 0
parse_read_num:
    mov al, [r8]
    add r8, 1
    cmp rax, '0'
    jl parse_add_num
    cmp rax, '9'
    jg parse_add_num
    sub rax, '0'
    imul rbx, 10
    add rbx, rax
    jmp parse_read_num
parse_add_num:
    mov [r9], ebx
    add r9, 4
    mov rbx, 0
parse_read_empty:
    mov al, [r8]
    add r8, 1
    cmp rax, 0
    je parse_end
    cmp al, 10
    je parse_add_num
    cmp rax, '0'
    jl parse_read_empty
    cmp rax, '9'
    jg parse_read_empty
    sub r8, 1
    jmp parse_read_num
parse_end:
    mov dword [r9], -1
    ret


; rax: result
; rbx: current num
; rcx: next num
; r8: nums pointer
; r9: nums pointer
a1:
    mov rax, 0
    mov r8, ns
a1_inc_ini:
    mov r9, r8
a1_inc:
    mov ebx, [r9]
    mov ecx, [r9 + 4]
    cmp rcx, 0
    jz a1_count
    sub rcx, rbx
    cmp rcx, 1
    jl a1_dec_ini
    cmp rcx, 3
    jg a1_dec_ini
    add r9, 4
    jmp a1_inc
a1_dec_ini:
    mov r9, r8
a1_dec:
    mov ebx, [r9]
    mov ecx, [r9 + 4]
    cmp rcx, 0
    jz a1_count
    sub rbx, rcx
    cmp rbx, 1
    jl a1_next
    cmp rbx, 3
    jg a1_next
    add r9, 4
    jmp a1_dec
a1_count:
    add rax, 1
a1_next:
    mov ebx, [r8]
    add r8, 4
    cmp rbx, 0
    jne a1_next
    mov ebx, [r8]
    cmp ebx, -1
    jne a1_inc_ini
a1_end:
    ret
