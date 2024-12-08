%define SIZE 50

struc pt
    .x  resq 1
    .y  resq 1
endstruc

%macro swap 2
    xor %1, %2
    xor %2, %1
    xor %1, %2
%endmacro


section .data
    INPUT:  incbin "../inputs/d08"
            db 0
    FMT:    db "%d", 10, 0
    freq:   db "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"


section .bss
%assign i 0
%rep 62
    ls%+i:  resb 10 * pt_size
%assign i i+1
%endrep
    ls_end: resq 1
    ls_ref: resq 256 ; freq ~> ls_


section .text
    global main
    extern printf
    default rel


main:
    sub rsp, 40

    call fill_ls_ref
    call find_antenna
    call fill_ls_ref

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


; rax: frequency
; rbx: list address
; rcx: ref entry pointer
fill_ls_ref:
%assign i 0
%rep 62
    xor rax, rax
    mov al, [freq + i]
    imul rax, 8
    lea rbx, [ls%+i]
    lea rcx, [ls_ref]
    add rcx, rax
    mov [rcx], rbx
%assign i i+1
%endrep
    ret


; rax: current char
; rbx: input pointer
; rcx: ls pointer
; rdx: ref entry pointer
; r8: x pos
; r9: y pos
find_antenna:
    lea rbx, [INPUT]
    mov r9, -1
.next_line:
    mov r8, -1
    add r9, 1
.next:
    ; get char
    xor rax, rax
    mov al, [rbx]
    add rbx, 1
    cmp al, 0
    jz .end
    ; increment pos
    cmp al, 10 ; \n
    je .next_line
    add r8, 1
    ; get reference
    imul rax, 8
    lea rdx, [ls_ref]
    add rdx, rax
    mov rcx, [rdx]
    cmp rcx, 0
    jz .next
    ; add to list
    mov [rcx + pt.x], r8
    mov [rcx + pt.y], r9
    add qword [rdx], pt_size
    jmp .next
.end:
    mov qword [ls_end], -1
    ret


a1:
    mov r13, 0
    jmp a12
a2:
    mov r13, 1
    jmp a12


; rax: temp
; r8: ls (i) pointer
; r9: ls (j) pointer
; r10: ls (start) pointer
; r13: harmonics flag (parameter)
a12:
    lea r10, [ls0 - pt_size]
    mov r8, r10
.next_i:
    add r8, pt_size
    mov rax, [r8]
    cmp rax, 0
    jz .next_ls
    mov r9, r8
.next_j:
    add r9, pt_size
    mov rax, [r9]
    cmp rax, 0
    jz .next_i
    call add_antinode
    swap r8, r9
    call add_antinode
    swap r8, r9
    jmp .next_j
.next_ls:
    add r10, 10 * pt_size
    mov r8, r10
    mov rax, [r8 + pt_size]
    cmp rax, -1
    jne .next_i
    ; prepare counting
; rax: result
; rbx: current char
; r8: input pointer
    xor rax, rax
    xor rbx, rbx
    lea r8, [INPUT]
.count:
    mov bl, [r8]
    add r8, 1
    cmp bl, 0
    jz .end
    cmp bl, '#'
    jne .count
    add rax, 1
    jmp .count
.end:
    ret


; rax: x pos
; rbx: y pos
; rcx: y pos * INPUT_LINE_WIDTH
; rdx: input position
; r8: ls (i) pointer (parameter)
; r9: ls (j) pointer (parameter)
; r11: dx
; r12: dy
; r13: harmonics flag (parameter)
add_antinode:
    mov rax, [r8 + pt.x]
    mov rbx, [r8 + pt.y]
    mov r11, rax
    sub r11, [r9 + pt.x]
    mov r12, rbx
    sub r12, [r9 + pt.y]
    ; harmonics
    cmp r13, 0
    jnz .write
.next:
    ; x
    add rax, r11
    cmp rax, 0
    jl .end
    cmp rax, SIZE
    jge .end
    ; y
    add rbx, r12
    cmp rbx, 0
    jl .end
    cmp rbx, SIZE
    jge .end
.write:
    lea rdx, [INPUT]
    add rdx, rax
    mov rcx, rbx
    imul rcx, SIZE + 2 ; \r\n
    add rdx, rcx
    mov byte [rdx], '#'
    ; harmonics
    cmp r13, 0
    jnz .next
.end:
    ret
