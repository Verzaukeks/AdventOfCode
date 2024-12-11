%define CACHE_MAX_NUM 7 ; higher is possible and faster, but 7 is funnier and fast enough

section .data
    INPUT:  incbin "../inputs/d11"
            db 10, 0
    FMT:    db "%lld", 10, 0

section .bss
    ns:     resq 10
    cache:  resq (CACHE_MAX_NUM + 1) * 76

section .text
    global main
    extern printf
    default rel


struc dp_para
    .num    resb 8
    .step   resb 1
endstruc

%macro dp_call 2
    sub rsp, dp_para_size
    mov qword [rsp + dp_para.num], %1
    mov byte [rsp + dp_para.step], %2
    call dp
    add rsp, dp_para_size
%endmacro


main:
    sub rsp, 40

    call parse

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


; rax: current char
; rbx: current number
; r8: input pointer
; r9: nums pointer
parse:
    xor rax, rax
    xor rbx, rbx
    lea r8, [INPUT]
    lea r9, [ns]
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


%macro run 1
    lea rax, [ns]
    push rax
    push 0
%%next:
    mov rax, [rsp + 8]
    add qword [rsp + 8], 8
    mov rbx, [rax]
    cmp rbx, -1
    je %%end
    dp_call rbx, %1
    add [rsp], rax
    jmp %%next
%%end:
    mov rax, [rsp]
    add rsp, 16
    ret
%endmacro

a1: run 25
a2: run 75


dp:
    mov rax, [rsp + 8 + dp_para.num]
    xor rbx, rbx
    mov bl, [rsp + 8 + dp_para.step]
.test:
    cmp qword [rsp + 8 + dp_para.num], CACHE_MAX_NUM
    jg .no_cache

    mov rbx, [rsp + 8 + dp_para.num]
    imul rbx, 76
    xor rcx, rcx
    mov cl, [rsp + 8 + dp_para.step]
    add rbx, rcx
    imul rbx, 8
    lea rcx, [cache]
    add rbx, rcx

    mov rax, [rbx]
    cmp rax, 0
    jnz .end

    call apply_rules

    mov rbx, [rsp + 8 + dp_para.num]
    imul rbx, 76
    xor rcx, rcx
    mov cl, [rsp + 8 + dp_para.step]
    add rbx, rcx
    imul rbx, 8
    lea rcx, [cache]
    add rbx, rcx

    mov [rbx], rax
.end: ret
.no_cache:
    call apply_rules
    ret


apply_rules:
    cmp byte [rsp + 16 + dp_para.step], 0
    jnz .rule1
    mov rax, 1
    ret
.rule1:
    cmp qword [rsp + 16 + dp_para.num], 0
    jnz .rule2
    mov al, [rsp + 16 + dp_para.step]
    sub al, 1
    dp_call 1, al
    ret
.rule2:
    mov rax, [rsp + 16 + dp_para.num]
    call split
    cmp rax, 0
    jz .rule3
    xor rax, rax
    mov al, [rsp + 16 + dp_para.step]
    sub al, 1
    push rcx
    dp_call rbx, al
    pop rbx
    push rax
    xor rax, rax
    mov al, [rsp + 16 + 8 + dp_para.step]
    sub al, 1
    dp_call rbx, al
    pop rbx
    add rax, rbx
    ret
.rule3:
    mov rax, [rsp + 16 + dp_para.num]
    imul rax, 2024
    xor rbx, rbx
    mov bl, [rsp + 16 + dp_para.step]
    sub bl, 1
    dp_call rax, bl
.end:
    ret


split:
    mov r8, rax
    xor r9, r9
.count:
    xor rdx, rdx
    mov rbx, 10
    div rbx
    add r9, 1
    cmp rax, 0
    jnz .count
    mov rax, r9
    and rax, 1
    cmp rax, 0
    jnz .fail
    shr r9, 1
    mov rax, r8
    xor rcx, rcx
    mov r10, 1
.step:
    xor rdx, rdx
    mov rbx, 10
    div rbx
    imul rdx, r10
    imul r10, 10
    add rcx, rdx
    sub r9, 1
    cmp r9, 0
    jnz .step
    mov rbx, rax
    mov rax, 1
    ret
.fail:
    xor rax, rax
    xor rbx, rbx
    xor rcx, rcx
    ret
