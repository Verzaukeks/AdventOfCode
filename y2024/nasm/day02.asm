; why am i even doing this...

section .data
    INPUT:  incbin "../inputs/d02"
    FMT:    db "%d", 10, 0

section .bss
    ns:     resd 10000
    ts:     resd 10

section .text
    global main
    extern printf
    default rel


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


; r8: result
; r9: nums pointer
; r10: current nums pointer
; r11: current temps pointer
; r12: skip index (pointer)
a2:
    xor r8, r8
    lea r9, [ns]
a2_ini:
    mov r12, r9
    sub r12, 4
a2_prepare_ini:
    mov r10, r9
    lea r11, [ts]
a2_prepare:
    mov eax, [r10]
    add r10, 4
    cmp eax, 0
    jz a2_test
    mov rbx, r12
    add rbx, 4
    cmp rbx, r10
    je a2_prepare
    mov [r11], eax
    add r11, 4
    jmp a2_prepare
a2_test:
    mov dword [r11], 0
    call safe_ts
    cmp rax, 0
    jz a2_skip_next
    add r8, 1
    jmp a2_next
a2_skip_next:
    add r12, 4
    mov eax, [r12]
    cmp eax, 0
    jnz a2_prepare_ini
a2_next:
    mov eax, [r9]
    add r9, 4
    cmp rax, 0
    jnz a2_next
    mov eax, [r9]
    cmp eax, -1
    jne a2_ini
a2_end:
    mov rax, r8
    ret


; rax: result
; rbx: temps pointer
; rcx: current num
; rdx: next num
safe_ts:
    xor rax, rax
    lea rbx, [ts]
safe_ts_inc:
    mov ecx, [rbx]
    mov edx, [rbx + 4]
    cmp rdx, 0
    jz safe_ts_success
    sub rdx, rcx
    cmp rdx, 1
    jl safe_ts_dec_ini
    cmp rdx, 3
    jg safe_ts_dec_ini
    add rbx, 4
    jmp safe_ts_inc
safe_ts_dec_ini:
    lea rbx, [ts]
safe_ts_dec:
    mov ecx, [rbx]
    mov edx, [rbx + 4]
    cmp rdx, 0
    jz safe_ts_success
    sub rcx, rdx
    cmp rcx, 1
    jl safe_ts_fail
    cmp rcx, 3
    jg safe_ts_fail
    add rbx, 4
    jmp safe_ts_dec
safe_ts_success:
    mov rax, 1
safe_ts_fail:
    ret