
section .data
    INPUT:  incbin "../inputs/d05"
            db 0, 10, 0
    FMT:    db "%d", 10, "%d", 0

section .bss
    rs:     resb 6000
    ms:     resb 6000

section .text
    global main
    extern printf
    default rel


main:
    sub rsp, 40

    call parse

    call a1
    lea rcx, [FMT]
    mov rdx, r10
    mov r8, r11
    call printf
    
    add rsp, 40
    xor rax, rax
    ret


%macro read_push_num 0
    mov al, [r8]
    mov bl, [r8 + 1]
    sub al, '0'
    sub bl, '0'
    imul rax, rax, 10
    add rbx, rax
    mov [r9], bl
    add r9, 1
%endmacro

; rax: current char/number
; rbx: current char/number
; r8: input pointer
; r9: rs/ms pointer
parse:
    xor rax, rax
    xor rbx, rbx
    lea r8, [INPUT]
    lea r9, [rs]
parse_rule:
    mov al, [r8 + 2]
    cmp al, '|'
    jne parse_manual_ini
    read_push_num
    add r8, 3
    read_push_num
    add r8, 4
    jmp parse_rule
parse_manual_ini:
    mov byte [r9], -1
    add r8, 2
    lea r9, [ms]
parse_manual:
    read_push_num
    add r8, 3
    mov al, [r8]
    cmp al, 10 ; \n
    jne parse_manual
    mov byte [r9], 0
    add r9, 1
    add r8, 1
    mov al, [r8]
    cmp al, 0
    jne parse_manual
parse_end:
    mov byte [r9], -1
    ret


; r8: manual pointer
; r9: next manual pointer
; r10: result (for a1)
; r11: result (for a2)
a1:
a2:
    lea r8, [ms]
    mov r9, r8
    xor r10, r10
    xor r11, r11
a12_next:
    add r9, 1
    mov al, [r9]
    cmp al, 0
    jg a12_next
    call calc_manual
    add r9, 1
    mov r8, r9
    mov al, [r8]
    cmp al, -1
    jne a12_next
a12_end:
    ret


; rax: fixed manual flag
; rbx: manual pointer
; rcx: current number
; rdx: rule pointer
; r8: manual start (parameter)
; r9: manual end (parameter)
; r10: result (shared for a1)
; r11: result (shared for a2)
; r12: rule num a
; r13: rule num b
; r14: index of num a
; r15: index of num b
calc_manual:
    xor rax, rax
    xor rcx, rcx
    lea rdx, [rs]
    xor r12, r12
    xor r13, r13
calc_manual_next:
    mov rbx, r8
    mov r12b, [rdx]
    mov r13b, [rdx + 1]
    mov r14, r8
    mov r15, r9
calc_manual_next_num:
    mov cl, [rbx]
    add rbx, 1
    cmp cl, r12b
    je calc_manual_next_num_hit1
    cmp cl, r13b
    je calc_manual_next_num_hit2
    cmp cl, 0
    jnz calc_manual_next_num
    cmp r14, r15
    jg calc_manual_fix
    add rdx, 2
    mov cl, [rdx]
    cmp cl, -1
    je calc_manual_end
    jmp calc_manual_next
calc_manual_fix:
    mov rax, 1
    lea rdx, [rs]
    mov sil, [r14 - 1]
    mov dil, [r15 - 1]
    mov [r14 - 1], dil
    mov [r15 - 1], sil
    jmp calc_manual_next
calc_manual_next_num_hit1:
    mov r14, rbx
    jmp calc_manual_next_num
calc_manual_next_num_hit2:
    mov r15, rbx
    jmp calc_manual_next_num
calc_manual_end:
    mov rbx, r8
    add rbx, r9
    shr rbx, 1
    cmp rax, 0
    jnz calc_manual_a2
calc_manual_a1:
    mov al, [rbx]
    add r10, rax
    ret
calc_manual_a2:
    mov al, [rbx]
    add r11, rax
    ret
