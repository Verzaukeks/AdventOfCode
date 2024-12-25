
section .data
    INPUT:  incbin "../inputs/d25"
            db 0, 0, 0, 0, 0
    FMT:    db "%lld", 10, 0

section .text
    global main
    extern printf
    default rel


main:
    sub rsp, 32

    call a1
    lea rcx, [FMT]
    mov rdx, rax
    xor rax, rax
    call printf

    add rsp, 32
    xor rax, rax
    ret


a1:
    xor rax, rax
    xor rbx, rbx
    lea rsi, [INPUT - 51]
.next_rsi:
    add rsi, 51
    cmp byte [rsi], 0
    je .end
    mov rdi, rsi
.next_rdi:
    add rdi, 51
    cmp byte [rdi], 0
    je .next_rsi
%assign i 0
%rep 49
    mov bl, [rsi + i]
    add bl, [rdi + i]
    cmp bl, '#' + '#'
    je .next_rdi
%assign i i+1
%endrep
    add rax, 1
    jmp .next_rdi
.end:
    ret
