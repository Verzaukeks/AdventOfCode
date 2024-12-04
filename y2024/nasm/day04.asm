%define SIZE 140

section .data
    INPUT:  incbin "../inputs/d04"
    FMT:    db "%d", 10, 0

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


%macro step 4 ; off_x, off_y, expected_char, on_fail
    ; x coord
    mov rbx, r8
    add rbx, %1
    cmp rbx, 0
    jl %4
    cmp rbx, SIZE
    jge %4
    ; y coord
    mov rcx, r9
    add rcx, %2
    cmp rcx, 0
    jl %4
    cmp rcx, SIZE
    jge %4
    ; coord
    lea rdx, [INPUT]
    imul rcx, SIZE + 2 ; \r\n
    add rdx, rcx
    add rdx, rbx
    ; char
    xor rax, rax
    mov al, [rdx]
    cmp al, %3
    jne %4
%endmacro


; r8: x coord
; r9: y coord
; r10: result
a1:
    xor r8, r8
    xor r9, r9
    xor r10, r10
a1_hor:
    step 0, 0, 'X', a1_next
    step 1, 0, 'M', a1_hor_rev
    step 2, 0, 'A', a1_hor_rev
    step 3, 0, 'S', a1_hor_rev
    add r10, 1
a1_hor_rev:
    step -1, 0, 'M', a1_ver
    step -2, 0, 'A', a1_ver
    step -3, 0, 'S', a1_ver
    add r10, 1
a1_ver:
    step 0, 1, 'M', a1_ver_rev
    step 0, 2, 'A', a1_ver_rev
    step 0, 3, 'S', a1_ver_rev
    add r10, 1
a1_ver_rev:
    step 0, -1, 'M', a1_dia1
    step 0, -2, 'A', a1_dia1
    step 0, -3, 'S', a1_dia1
    add r10, 1
a1_dia1:
    step 1, 1, 'M', a1_dia1_rev
    step 2, 2, 'A', a1_dia1_rev
    step 3, 3, 'S', a1_dia1_rev
    add r10, 1
a1_dia1_rev:
    step -1, -1, 'M', a1_dia2
    step -2, -2, 'A', a1_dia2
    step -3, -3, 'S', a1_dia2
    add r10, 1
a1_dia2:
    step -1, 1, 'M', a1_dia2_rev
    step -2, 2, 'A', a1_dia2_rev
    step -3, 3, 'S', a1_dia2_rev
    add r10, 1
a1_dia2_rev:
    step 1, -1, 'M', a1_next
    step 2, -2, 'A', a1_next
    step 3, -3, 'S', a1_next
    add r10, 1
a1_next:
    add r8, 1
    cmp r8, SIZE
    jne a1_hor
    xor r8, r8
    add r9, 1
    cmp r9, SIZE
    jne a1_hor
a1_end:
    mov rax, r10
    ret


; r8: x coord
; r9: y coord
; r10: result
a2:
    xor r8, r8
    xor r9, r9
    xor r10, r10
a2_1:
    step 0, 0, 'A', a2_next
    step -1, -1, 'M', a2_2
    step  1, -1, 'M', a2_2
    step  1,  1, 'S', a2_2
    step -1,  1, 'S', a2_2
    jmp a2_hit
a2_2:
    step -1, -1, 'S', a2_3
    step  1, -1, 'M', a2_3
    step  1,  1, 'M', a2_3
    step -1,  1, 'S', a2_3
    jmp a2_hit
a2_3:
    step -1, -1, 'S', a2_4
    step  1, -1, 'S', a2_4
    step  1,  1, 'M', a2_4
    step -1,  1, 'M', a2_4
    jmp a2_hit
a2_4:
    step -1, -1, 'M', a2_next
    step  1, -1, 'S', a2_next
    step  1,  1, 'S', a2_next
    step -1,  1, 'M', a2_next
a2_hit:
    add r10, 1
a2_next:
    add r8, 1
    cmp r8, SIZE
    jne a2_1
    xor r8, r8
    add r9, 1
    cmp r9, SIZE
    jne a2_1
a2_end:
    mov rax, r10
    ret
