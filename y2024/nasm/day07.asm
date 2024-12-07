
section .data
    INPUT:  incbin "../inputs/d07"
            db 10, 10, 0
    FMT:    db "%lld", 10, 0

section .bss
    ns:     resq 20000
    ops:    resb 11

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
    je .end
    cmp rax, 10
    je .add
    cmp rax, '0'
    jl .empty
    cmp rax, '9'
    jg .empty
    sub r8, 1
    jmp .number
.end:
    mov qword [r9], -1
    ret


; r8: result
; r9: ns pointer
; r10: ops pointer
a1:
    xor r8, r8
    lea r9, [ns]
    lea r10, [ops]
.test:
%assign i 0
%rep 2048
    mov byte [ops + 0x0], i >> 0x0 & 0b1
    mov byte [ops + 0x1], i >> 0x1 & 0b1
    mov byte [ops + 0x2], i >> 0x2 & 0b1
    mov byte [ops + 0x3], i >> 0x3 & 0b1
    mov byte [ops + 0x4], i >> 0x4 & 0b1
    mov byte [ops + 0x5], i >> 0x5 & 0b1
    mov byte [ops + 0x6], i >> 0x6 & 0b1
    mov byte [ops + 0x7], i >> 0x7 & 0b1
    mov byte [ops + 0x8], i >> 0x8 & 0b1
    mov byte [ops + 0x9], i >> 0x9 & 0b1
    mov byte [ops + 0xA], i >> 0xA & 0b1
    call check
    cmp rax, 1
    je .success
%assign i i+1
%endrep
    jmp .next_line
.success:
    add r8, [r9]
.next_line:
    mov rax, [r9]
    add r9, 8
    cmp rax, 0
    jnz .next_line
    mov rax, [r9]
    cmp rax, -1
    jne .test
.end:
    mov rax, r8
    ret


; rax: current num (temp)
; rbx: current num
; rcx: number
; rdx: (reserved for divison) [div rbx => rdx:rax / rbx]
; r9: ns pointer ('parameter')
; r10: ops pointer ('parameter')
; r11: index
; r12: current op
; r13: 10
check:
    mov rcx, [r9 + 8]
    xor r11, r11
    xor r12, r12
    mov r13, 10
.next:
    cmp rcx, [r9]
    jg .fail
    mov r12b, [r10 + r11]
    mov rbx, [r9 + 8 * r11 + 16]
    add r11, 1
    cmp rbx, 0
    jz .test
    cmp r12b, 1
    je .mul
    cmp r12b, 2
    je .concat
.add:
    add rcx, rbx
    jmp .next
.mul:
    imul rcx, rbx
    jmp .next
.concat:
    mov rax, rbx
.concat_next:
    xor rdx, rdx
    div r13
    imul rcx, r13
    cmp rax, 0
    jnz .concat_next
    add rcx, rbx
    jmp .next
.test:
    cmp rcx, [r9]
    jne .fail
.success:
    mov rax, 1
    ret
.fail:
    xor rax, rax
    ret


; r8: result
; r9: ns pointer
; r10: ops pointer
a2:
    xor r8, r8
    lea r9, [ns]
    lea r10, [ops]
.test:
%assign i 0
%rep 177147 ; yeah, definitly efficient...
    ; %assign j 0
    ; %assign k 1
    ; %rep 11
    ;     %if ((i / k) % 3) != (((i - 1) / k) % 3)
    ;         mov byte [ops + j], (i / k) % 3
    ;     %endif
    ; %assign j j+1
    ; %assign k k*3
    ; %endrep
    mov byte [ops + 0x0], (i /     1) % 3
    mov byte [ops + 0x1], (i /     3) % 3
    mov byte [ops + 0x2], (i /     9) % 3
    mov byte [ops + 0x3], (i /    27) % 3
    mov byte [ops + 0x4], (i /    81) % 3
    mov byte [ops + 0x5], (i /   243) % 3
    mov byte [ops + 0x6], (i /   729) % 3
    mov byte [ops + 0x7], (i /  2187) % 3
    mov byte [ops + 0x8], (i /  6561) % 3
    mov byte [ops + 0x9], (i / 19683) % 3
    mov byte [ops + 0xA], (i / 59049) % 3
    call check
    cmp rax, 1
    je .success
%assign i i+1
%endrep
    jmp .next_line
.success:
    add r8, [r9]
.next_line:
    mov rax, [r9]
    add r9, 8
    cmp rax, 0
    jnz .next_line
    mov rax, [r9]
    cmp rax, -1
    jne .test
.end:
    mov rax, r8
    ret