%define UNDEFINED -1

section .data
    INPUT:  incbin "../inputs/d19"
            db 0
    FMT:    db "%lld", 10, "%lld", 10, 0

section .bss
    ps:     resb 100000
    qs:     resb 100000
    cache:  resq 96

section .text
    global main
    extern printf
    default rel


main:
    sub rsp, 32

    call parse

    call a12
    lea rcx, [FMT]
    mov rdx, r8
    mov r8, r9
    xor rax, rax
    call printf

    add rsp, 32
    xor rax, rax
    ret


parse:
    xor rax, rax
    lea r8, [INPUT]
    lea r9, [ps]
.ps_next:
    mov al, [r8]
    add r8, 1

    cmp al, ' '
    je .ps_next
    cmp al, 13 ; \r
    je .ps_next

    mov byte [r9], 0
    add r9, 1

    cmp al, ','
    je .ps_next
    cmp al, 10 ; \n
    je .qs_start

    mov [r9 - 1], al
    jmp .ps_next
.qs_start:
    mov byte [r9], -1
    add r8, 2
    lea r9, [qs]
.qs_next:
    mov al, [r8]
    add r8, 1

    cmp al, 13
    je .qs_next
    
    mov byte [r9], 0
    add r9, 1

    cmp al, 0
    je .end
    cmp al, 10
    je .qs_next

    mov [r9 - 1], al
    jmp .qs_next
.end:
    mov byte [r9], -1
    ret


a1:
a2:
a12:
    lea rsi, [qs]
    lea rdi, [cache]
    xor r8, r8
    xor r9, r9

    sub rsp, 8
.test:
    mov [rsp], rsi
    call check_0

    xor rbx, rbx
    cmp rax, 0
    setne bl

    add r8, rbx
    add r9, rax
.next:
    add rsi, 1
    cmp byte [rsi - 1], 0
    jne .next

    cmp byte [rsi], -1
    je .end

    jmp .test
.end:
    add rsp, 8
    ret


check_0:
    %assign i 0
    %rep 96
    mov qword [cache + i], UNDEFINED
    %assign i i+8
    %endrep
check:
    mov rbx, [rsp + 8]
    sub rbx, rsi
    imul rbx, 8
    add rbx, rdi
    cmp qword [rbx], UNDEFINED
    jne .end

    mov qword [rbx], 1

    mov rax, [rsp + 8]
    cmp byte [rax], 0
    je .end

    mov qword [rbx], 0

    lea rbx, [ps]
    push rbx
.test:
    mov rbx, [rsp]
    mov rcx, [rsp + 16]
.test_next:
    mov al, [rbx]
    cmp al, 0
    jz .test_ok

    cmp al, [rcx]
    jne .test_fail

    add rbx, 1
    add rcx, 1
    jmp .test_next
.test_ok:
    push rcx
    call check
    add rsp, 8

    mov rbx, [rsp + 16]
    sub rbx, rsi
    imul rbx, 8
    add rbx, rdi
    add [rbx], rax
.test_fail:
    mov rbx, [rsp]
    add qword [rsp], 1

    cmp byte [rbx], 0
    jne .test_fail

    cmp byte [rbx + 1], -1
    jne .test

    add rsp, 8
.end:
    mov rbx, [rsp + 8]
    sub rbx, rsi
    imul rbx, 8
    add rbx, rdi
    mov rax, [rbx]
    ret