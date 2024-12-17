
section .data
    INPUT:  incbin "../inputs/d17"
            db 0
    FMT:    db "%lld,", 0
    NL:     db 10, 0

    regs:   dq 0, 1, 2, 3
    regA:   dq 0
    regB:   dq 0
    regC:   dq 0
    ops:    dq adv_, bxl_, bst_, jnz_, bxc_, out_, bdv_, cdv_

section .bss
    ns:     resq 3
    prog:   resq 100
    prog2:  resq 100

section .text
    global main
    extern printf
    default rel


main:
    sub rsp, 32

    call parse
    call a1
    lea rcx, [NL]
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
    jmp .empty
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
    jmp .number
.end:
    mov qword [r9], -1
    ret


adv_:
    lea rbx, [regs]
    mov rcx, [rbx + rcx * 8]
    shr qword [regA], cl
    ret
bxl_:
    xor qword [regB], rcx
    ret
bst_:
    lea rbx, [regs]
    mov rcx, [rbx + rcx * 8]
    and rcx, 0b111
    mov [regB], rcx
    ret
jnz_:
    cmp qword [regA], 0
    jz .end
    mov rsi, rcx
.end:
    ret
bxc_:
    mov rcx, [regC]
    xor [regB], rcx
    ret
out_:
    lea rbx, [regs]
    mov rdx, [rbx + rcx * 8]
    and rdx, 0b111
    sub rsp, 32
    lea rcx, [FMT]
    xor rax, rax
    call printf
    add rsp, 32
    ret
bdv_:
    mov rax, [regA]
    lea rbx, [regs]
    mov rcx, [rbx + rcx * 8]
    shr rax, cl
    mov [regB], rax
    ret
cdv_:
    mov rax, [regA]
    lea rbx, [regs]
    mov rcx, [rbx + rcx * 8]
    shr rax, cl
    mov [regC], rax
    ret


a1:
    mov rax, [ns +  0]
    mov rbx, [ns +  8]
    mov rcx, [ns + 16]
    mov [regA], rax
    mov [regB], rbx
    mov [regC], rcx

    xor rsi, rsi
    lea rdi, [prog]
.run:
    lea rax, [ops]
    mov rbx, [rdi + rsi * 8]
    mov rcx, [rdi + rsi * 8 + 8]
    cmp rbx, -1
    je .end

    add rsi, 2
    call [rax + rbx * 8]
    jmp .run
.end:
    ret



modified_out:
    lea rbx, [regs]
    mov rdx, [rbx + rcx * 8]
    and rdx, 0b111
    mov [r13], rdx ; write into prog2 instead of stdout
    add r13, 8
    ret

a2:
    lea rax, [modified_out]
    mov qword [ops + 5 * 8], rax
    mov qword [ns], 0

%assign i 1
%rep 16
    shl qword [ns], 3
    sub qword [ns], 1
.test%+i:
    lea r13, [prog2]
    add qword [ns], 1
    call a1

    lea rsi, [prog + (16 - i) * 8]
    lea rdi, [prog2]
    %rep i
    mov rax, [rsi]
    cmp rax, [rdi]
    jne .test%+i
    add rsi, 8
    add rdi, 8
    %endrep
%assign i i+1
%endrep

    mov rax, [ns]
    ret
