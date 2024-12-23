%define MAXN (26*26)
%define _t_ ('t' - 'a')

section .data
    INPUT:  incbin "../inputs/d23"
            db 0, 0, 0
    FMT:    db "%lld", 10, 0

section .bss
    adj:    resb MAXN * MAXN
    sol:    resb 100

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
    lea rcx, [sol]
    xor rax, rax
    call printf

    add rsp, 32
    xor rax, rax
    ret


parse:
    lea r8, [INPUT]
    lea r9, [adj]
.next:
    xor rax, rax
    xor rbx, rbx
    xor rcx, rcx
    xor rdx, rdx

    mov al, [r8]
    mov bl, [r8 + 1]
    mov cl, [r8 + 3]
    mov dl, [r8 + 4]

    sub rax, 'a'
    sub rbx, 'a'
    sub rcx, 'a'
    sub rdx, 'a'
    imul rax, 26
    imul rcx, 26
    add rax, rbx
    add rcx, rdx

    mov rsi, rax
    mov rdi, rcx
    imul rsi, MAXN
    imul rdi, MAXN
    add rsi, rcx
    add rdi, rax

    mov byte [r9 + rsi], 1
    mov byte [r9 + rdi], 1

    add r8, 7
    cmp byte [r8], 0
    jne .next
    ret


a1:
    xor r8, r8
    lea r9, [adj]
    mov r10, -1
.i:
    add r10, 1
    mov r11, r10

    cmp r10, MAXN
    je .end

    mov  rsi, r10
    imul rsi, MAXN
    add  rsi, r9
.j:
    add r11, 1
    mov r12, r11

    cmp r11, MAXN
    je .i

    cmp byte [rsi + r11], 0
    jz .j

    mov  rdi, r11
    imul rdi, MAXN
    add  rdi, r9
.k:
    add r12, 1

    cmp r12, MAXN
    je .j

    cmp byte [rsi + r12], 0
    jz .k
    cmp byte [rdi + r12], 0
    jz .k

    mov rcx, 26

    mov rax, r10
    xor rdx, rdx
    idiv rcx
    cmp rax, _t_
    je .inc

    mov rax, r11
    xor rdx, rdx
    idiv rcx
    cmp rax, _t_
    je .inc

    mov rax, r12
    xor rdx, rdx
    idiv rcx
    cmp rax, _t_
    je .inc

    jmp .k
.inc:
    add r8, 1
    jmp .k
.end:
    mov rax, r8
    ret


a2:
    xor r8, r8 ; current set size
    xor r9, r9 ; sol     set size
    mov byte [sol], '?'

    sub rsp, 8
    mov qword [rsp], -1
    call deep
    add rsp, 8
    ret


deep:
    add r8, 1
    sub rsp, 8
    mov rax, [rsp + 16]
    mov [rsp], rax

.next: ; check next computer
    add qword [rsp], 1
    cmp qword [rsp], MAXN
    je .end
.next_continue:

.cmp: ; check for connections
    mov  rax, [rsp]
    lea  rbx, [adj]
    imul rax, MAXN
    add  rax, rbx

    mov rsi, r8
    add rsi, r8
.cmp_next:
    sub rsi, 2
    cmp rsi, 0
    jz .cmp_continue

    mov rbx, [rsp + rsi * 8]
    cmp byte [rax + rbx], 0
    jz .next
    jmp .cmp_next
.cmp_continue:

    call deep ; try to increase set

.test: ; check if current solution is better and contains a t- computer
    cmp r8, r9
    jle .next

    mov rsi, r8
    add rsi, r8
.test_next:
    sub rsi, 2
    cmp rsi, 0
    jl .next

    mov rax, [rsp + rsi * 8]
    mov rcx, 26
    xor rdx, rdx
    idiv rcx
    cmp rax, _t_
    jne .test_next
.test_continue:

    mov r9, r8 ; update sol

.print:
    mov rsi, r8
    add rsi, r8
    lea rdi, [sol]
.print_next:
    sub rsi, 2
    cmp rsi, 0
    jl .print_continue

    mov rax, [rsp + rsi * 8]
    mov rcx, 26
    xor rdx, rdx
    idiv rcx

    add rax, 'a'
    add rdx, 'a'
    mov [rdi], rax
    mov [rdi + 1], rdx
    mov byte [rdi + 2], ','
    add rdi, 3

    jmp .print_next
.print_continue:
    mov byte [rdi - 1], 0

    jmp .next
    
.end:
    add rsp, 8
    sub r8, 1
    ret
