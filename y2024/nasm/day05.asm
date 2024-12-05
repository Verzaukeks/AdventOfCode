%define SIZE 140

section .data
    INPUT:  incbin "../inputs/d05"
            db 0

    FMT:    db "%d", 10, 0
    rsize:  dq 0
    psize:  dq 0

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

    ; call a1
    ; lea rcx, [FMT]
    ; mov rdx, rax
    ; call printf

    ; call a2
    ; lea rcx, [FMT]
    ; mov rdx, rax
    ; call printf
    
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

; rax: current char
; rbx: current number
; r8: input pointer
; r9: rs/ps pointer
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
