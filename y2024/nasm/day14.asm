%define WIDTH 101
%define HEIGHT 103


section .data
    INPUT:  incbin "../inputs/d14"
            db 0
    FMT:    db "%lld", 10, 0
    OUTPUT:
        %rep HEIGHT
            times WIDTH db ' '
            db 10
        %endrep
        db 0

section .text
    global main
    extern printf
    default rel


main:
    mov rax, [rsp + 8]
    sub rsp, 40

    call a2
    lea rcx, [OUTPUT]
    call printf

    call a1
    lea rcx, [FMT]
    mov rdx, rax
    call printf

    add rsp, 40
    xor rax, rax
    ret


struc para
    .q1     resq 1
    .q2     resq 1
    .q3     resq 1
    .q4     resq 1

    .ptr    resq 1

    .px     resq 1
    .py     resq 1
    .vx     resq 1
    .vy     resq 1
endstruc


parse:
    call .read
    cmp rax, 0
    jz .fail
    mov [rsp + 8 + para.px], rbx
    call .read
    mov [rsp + 8 + para.py], rbx
    call .read
    mov [rsp + 8 + para.vx], rbx
    call .read
    mov [rsp + 8 + para.vy], rbx
    mov rax, 1
    ret
.read:
    xor rax, rax
    mov rsi, [rsp + 16 + para.ptr]
    xor r8, r8
    xor r9, r9
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
    mov al, [rsi - 1]
    cmp al, '-'
    sete r9b
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
    cmp r9, 0
    jz .success_ret
    neg r8
.success_ret:
    mov rax, 1
    mov rbx, r8
    mov [rsp + 16 + para.ptr], rsi
    ret
.fail:
    xor rax, rax
    xor rbx, rbx
    ret


a1:
    sub rsp, para_size
    mov qword [rsp + para.q1], 0
    mov qword [rsp + para.q2], 0
    mov qword [rsp + para.q3], 0
    mov qword [rsp + para.q4], 0
    lea rax, [INPUT]
    mov [rsp + para.ptr], rax
.next:
    call parse
    cmp rax, 0
    jz .end

    xor r8, r8
    xor r9, r9

    cmp qword [rsp + para.vx], 0
    jge .x_pos
    add qword [rsp + para.vx], WIDTH
.x_pos:
    cmp qword [rsp + para.vy], 0
    jge .y_pos
    add qword [rsp + para.vy], HEIGHT
.y_pos:

    ; (px + vx * 100) % WIDTH 
    mov rax, [rsp + para.vx]
    imul rax, 100
    add rax, [rsp + para.px]
    mov rdx, rax
    sar rdx, 63
    mov rbx, WIDTH
    idiv rbx

    cmp rdx, WIDTH / 2
    je .next
    setg r8b

    ; (py + vy * 100) % HEIGHT
    mov rax, [rsp + para.vy]
    imul rax, 100
    add rax, [rsp + para.py]
    mov rdx, rax
    sar rdx, 63
    mov rbx, HEIGHT
    idiv rbx

    cmp rdx, HEIGHT / 2
    je .next
    setg r9b

    ; quadrant
    mov rax, r8
    shl rax, 1
    add rax, r9
    shl rax, 3
    add qword [rsp + para.q1 + rax], 1
    
    jmp .next
.end:
    mov  rax, [rsp + para.q1]
    imul rax, [rsp + para.q2]
    imul rax, [rsp + para.q3]
    imul rax, [rsp + para.q4]
    add rsp, para_size
    ret


%ifndef STEPS
%define STEPS 6355
%endif
a2:
    sub rsp, para_size
    lea rax, [INPUT]
    mov [rsp + para.ptr], rax
.next:
    call parse
    cmp rax, 0
    jz .end

    cmp qword [rsp + para.vx], 0
    jge .x_pos
    add qword [rsp + para.vx], WIDTH
.x_pos:
    cmp qword [rsp + para.vy], 0
    jge .y_pos
    add qword [rsp + para.vy], HEIGHT
.y_pos:

    ; (px + vx * STEPS) % WIDTH 
    mov rax, [rsp + para.vx]
    imul rax, STEPS
    add rax, [rsp + para.px]
    mov rdx, rax
    sar rdx, 63
    mov rbx, WIDTH
    idiv rbx
    mov [rsp + para.px], rdx

    ; (py + vy * STEPS) % HEIGHT
    mov rax, [rsp + para.vy]
    imul rax, STEPS
    add rax, [rsp + para.py]
    mov rdx, rax
    sar rdx, 63
    mov rbx, HEIGHT
    idiv rbx
    mov [rsp + para.py], rdx
    
    ; print
    mov rax, [rsp + para.py]
    imul rax, WIDTH + 1
    add rax, [rsp + para.px]
    lea rbx, [OUTPUT]
    mov byte [rax + rbx], '*'

    jmp .next
.end:
    mov  rax, [rsp + para.q1]
    imul rax, [rsp + para.q2]
    imul rax, [rsp + para.q3]
    imul rax, [rsp + para.q4]
    add rsp, para_size
    ret
