; why am i even doing this...

section .data
    INPUT:  incbin "../inputs/d01"
            db 10, 0

    FMT:    db "%d", 10, 0
    asize:  dd 0
    tsize:  dd 0

section .bss
    as:     resd 1000
    bs:     resd 1000
    ts:     resd 2000

section .text
    global main
    extern printf


main:
    push rbp

    ; lea rcx, [rel INPUT]
    ; call printf

    call parse

    mov rcx, as
    mov edx, [rel asize]
    call sort

    mov rcx, bs
    mov edx, [rel asize]
    call sort

    call a1
    lea rcx, [rel FMT]
    mov rdx, rax
    call printf

    call a2
    lea rcx, [rel FMT]
    mov rdx, rax
    call printf
    
    pop rbp
    xor rax, rax
    ret


; rax: current char
; rbx: current number
; r8: input pointer
; r9: temps pointer
parse:
    mov rbx, 0
    mov r8, INPUT
    mov r9, ts
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
    add dword [rel tsize], 1
    mov rbx, 0
parse_read_empty:
    mov al, [r8]
    add r8, 1
    cmp rax, 0
    je parse_shuffle
    cmp rax, '0'
    jl parse_read_empty
    cmp rax, '9'
    jg parse_read_empty
    sub r8, 1
    jmp parse_read_num
; rax: current num
; rbx: index
; rcx: size
; r8:  temps pointer
; r9:  as/bs pointer
parse_shuffle:
    mov rbx, 0
    mov ecx, [rel tsize]
    shr rcx, 1
    mov [rel asize], ecx
    mov r8, ts
    mov r9, as
parse_shuffle_as:
    mov eax, [r8 + rbx * 8]
    mov [r9 + rbx * 4], eax
    add rbx, 1
    cmp rbx, rcx
    jl parse_shuffle_as
    mov rbx, 0
    add r8, 4
    mov r9, bs
parse_shuffle_bs:
    mov eax, [r8 + rbx * 8]
    mov [r9 + rbx * 4], eax
    add rbx, 1
    cmp rbx, rcx
    jl parse_shuffle_bs
    ret


; rax: something changed flag
; rbx: index
; rcx: array base (parameter)
; rdx: array size (parameter)
; r8: current number
; r9: next number
sort:
    mov rax, 0
    mov rbx, 0
    sub rdx, 1
sort_next:
    mov r8d, [rcx + rbx * 4]
    mov r9d, [rcx + rbx * 4 + 4]
    cmp r8, r9
    jle sort_skip
    mov [rcx + rbx * 4], r9d
    mov [rcx + rbx * 4 + 4], r8d
    mov rax, 1
sort_skip:
    add rbx, 1
    cmp rbx, rdx
    jl sort_next
    cmp rax, 0
    jne sort
    ret


; rax: result/sum
; rbx: remaining number count
; rcx: number/difference/absolute
; r8: as pointer
; r9: bs pointer
a1:
    mov rax, 0
    mov rbx, 0
    mov r8, as
    mov r9, bs
a1_next:
    mov ecx, [r8 + rbx * 4]
    sub ecx, [r9 + rbx * 4]
    add rbx, 1
    ; absolute
    mov edx, ecx
    sar edx, 31
    xor ecx, edx
    sub ecx, edx
    ; sum & next
    add eax, ecx
    cmp ebx, [rel asize]
    jl a1_next
    ret


; rax: result/sum
; rbx: number
; r8: as pointer
; r9: bs pointer
; r10: as end pointer
; r11: bs end pointer
; r12: count of number in as
; r13: count of number in bs
a2:
    mov rax, 0
    mov r8, as
    mov r9, bs
    imul r10d, [rel asize], 4
    imul r11d, [rel asize], 4
    add r10, r8
    add r11, r9
a2_next:
    mov ebx, [r8]
    add r8, 4
    mov r12, 1
    mov r13, 0
a2_count_as:
    cmp r8, r10
    je a2_count_bs
    cmp ebx, [r8]
    jne a2_count_bs
    add r8, 4
    add r12, 1
    jmp a2_count_as
a2_count_bs_next:
    add r9, 4
a2_count_bs:
    cmp r9, r11
    je a2_calc
    cmp ebx, [r9]
    jg a2_count_bs_next
    jne a2_calc
    add r13, 1
    jmp a2_count_bs_next
a2_calc:
    imul rbx, r12
    imul rbx, r13
    add rax, rbx
    cmp r8, r10
    je a2_end
    cmp r9, r11
    je a2_end
    jmp a2_next
a2_end:
    ret
