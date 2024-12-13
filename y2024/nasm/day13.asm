
section .data
    INPUT:  incbin "../inputs/d13"
            db 0
    FMT:    db "%lld", 10, 0

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


struc para
    .sum    resq 1
    .ptr    resq 1
    .x0     resq 1
    .y0     resq 1
    .x1     resq 1
    .y1     resq 1
    .x2     resq 1
    .y2     resq 1
endstruc
parse:
    call .read
    cmp rax, 0
    jz .fail
    mov [rsp + 8 + para.x0], rax
    call .read
    mov [rsp + 8 + para.y0], rax
    call .read
    mov [rsp + 8 + para.x1], rax
    call .read
    mov [rsp + 8 + para.y1], rax
    call .read
    mov [rsp + 8 + para.x2], rax
    call .read
    mov [rsp + 8 + para.y2], rax
    mov rax, 1
    ret
.read:
    xor rax, rax
    mov rsi, [rsp + 16 + para.ptr]
    xor r8, r8
.empty:
    mov al, [rsi]
    cmp al, 0
    jz .fail
    add rsi, 1
    cmp al, '0'
    jl .empty
    cmp al, '9'
    jg .empty
    sub rsi, 1
.num:
    mov al, [rsi]
    cmp al, '0'
    jl .success
    cmp al, '9'
    jg .success
    add rsi, 1
    sub al, '0'
    imul r8, 10
    add r8, rax
    jmp .num
.success:
    mov rax, r8
    mov [rsp + 16 + para.ptr], rsi
    ret
.fail:
    xor rax, rax
    ret


a1:
    lea rax, [INPUT]
    sub rsp, para_size
    mov qword [rsp + para.sum], 0
    mov       [rsp + para.ptr], rax
.next:
    call parse
    cmp rax, 0
    jz .end

    xor r8, r8
    xor r9, r9
    mov r10, 0xeadbeef
.step:
    mov rax, [rsp + para.x0]
    mov rbx, [rsp + para.x1]
    imul rax, r8
    imul rbx, r9
    add rax, rbx
    cmp rax, [rsp + para.x2]
    jne .miss

    mov rax, [rsp + para.y0]
    mov rbx, [rsp + para.y1]
    imul rax, r8
    imul rbx, r9
    add rax, rbx
    cmp rax, [rsp + para.y2]
    jne .miss

    mov rax, r8
    imul rax, 3
    add rax, r9

    cmp rax, r10
    jge .miss
    mov r10, rax
.miss:
    add r8, 1
    cmp r8, 100
    jle .step

    xor r8, r8
    add r9, 1
    cmp r9, 100
    jle .step

    cmp r10, 0xeadbeef
    je .next

    add [rsp + para.sum], r10
    jmp .next
.end:
    mov rax, [rsp + para.sum]
    add rsp, para_size
    ret


a2:
    lea rax, [INPUT]
    sub rsp, para_size
    mov qword [rsp + para.sum], 0
    mov       [rsp + para.ptr], rax
.next:
    call parse
    cmp rax, 0
    jz .end

    mov rax, 10000000000000
    add [rsp + para.x2], rax
    add [rsp + para.y2], rax

    ; 1/d
    mov   r8, [rsp + para.x0]
    imul  r8, [rsp + para.y1]
    mov  rax, [rsp + para.x1]
    imul rax, [rsp + para.y0]
    sub r8, rax
    cmp r8, 0
    jz .kolinear

    ; a
    mov  rax, [rsp + para.x2]
    imul rax, [rsp + para.y1]
    mov  rbx, [rsp + para.x1]
    imul rbx, [rsp + para.y2]
    sub rax, rbx
    mov rdx, rax
    sar rdx, 63
    idiv r8
    mov r10, rax

    ; b
    mov  rax, [rsp + para.x0]
    imul rax, [rsp + para.y2]
    mov  rbx, [rsp + para.x2]
    imul rbx, [rsp + para.y0]
    sub rax, rbx
    mov rdx, rax
    sar rdx, 63
    idiv r8
    mov r11, rax

    ; check
    mov  rax, [rsp + para.x0]
    mov  rbx, [rsp + para.x1]
    imul rax, r10
    imul rbx, r11
    add  rax, rbx
    cmp  rax, [rsp + para.x2]
    jne .next

    mov  rax, [rsp + para.y0]
    mov  rbx, [rsp + para.y1]
    imul rax, r10
    imul rbx, r11
    add  rax, rbx
    cmp  rax, [rsp + para.y2]
    jne .next

    ; costs
    imul r10, 3
    add [rsp + para.sum], r10
    add [rsp + para.sum], r11
    jmp .next
.kolinear:
    ; there is no kolinear input => didn't bother
    mov rax, -1
    add rsp, para_size
    ret
.end:
    mov rax, [rsp + para.sum]
    add rsp, para_size
    ret
