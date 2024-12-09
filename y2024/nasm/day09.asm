%define TYPE_FREE 0
%define TYPE_FILE 1
%define TYPE_HEAD 2
%define TYPE_TAIL 3

struc node
    .prev   resb 8
    .next   resb 8
    .type   resb 1
    .id     resb 2
    .len    resb 1
endstruc


section .data
    INPUT:  incbin "../inputs/d09"
            db 0
    FMT:    db "%lld", 10, 0

section .bss
    ls:      resb 44000 * node_size
    ls_head: equ ls
    ls_tail: resq 1

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


a1:
    call create_ls
    call compact
    call evaluate
    ret


a2:
    call create_ls
    call compact2
    call evaluate
    ret


; rax: current char/length
; r8: current type
; r9: current index
; r10: input pointer
; r11: ls pointer
create_ls:
    xor rax, rax
    mov r8, TYPE_FILE
    mov r9, 0
    lea r10, [INPUT]
    lea r11, [ls]
    ; head
    ; mov qword [r11 + node.prev], 0
    mov byte  [r11 + node.type], TYPE_HEAD
    ; mov word  [r11 + node.id], 0
    ; mov byte  [r11 + node.len], 0
    add r11, node_size
    mov [r11 - node_size + node.next], r11
.next:
    mov al, [r10]
    add r10, 1
    cmp al, '0'
    jl .end
    sub al, '0'
    ; add node
    mov [r11 + node.prev], r11
    mov [r11 + node.next], r11
    sub qword [r11 + node.prev], node_size
    add qword [r11 + node.next], node_size
    mov [r11 + node.type], r8b
    mov [r11 + node.len], al
    cmp r8, TYPE_FILE
    jne .free_node
.file_node:
    mov [r11 + node.id], r9w
    add r11, node_size
    mov r8, TYPE_FREE
    add r9, 1
    jmp .next
.free_node:
    ; mov word [r11 + node.id], 0
    add r11, node_size
    mov r8, TYPE_FILE
    jmp .next
.end:
    ; tail
    mov [ls_tail], r11
    mov [r11 + node.prev], r11
    sub qword [r11 + node.prev], node_size
    ; mov qword [r11 + node.next], 0
    mov byte  [r11 + node.type], TYPE_TAIL
    ; mov word  [r11 + node.id], 0
    ; mov byte  [r11 + node.len], 0
    ret


; rax: temp
; rbx: temp pointer
; rcx: temp pointer
; r10: free node pointer
; r11: file node pointer
compact:
    xor rax, rax
    lea r10, [ls_head]
.find_free:
    mov r10, [r10 + node.next]
    mov al, [r10 + node.type]
    cmp al, TYPE_TAIL
    je .end
    cmp al, TYPE_FREE
    jne .find_free
    ; find_file init
    mov r11, [ls_tail]
.find_file:
    mov r11, [r11 + node.prev]
    cmp r10, r11
    je .end
    mov al, [r11 + node.type]
    cmp al, TYPE_HEAD
    je .end
    cmp al, TYPE_FILE
    jne .find_file
    ; check
    mov al, [r10 + node.len]
    cmp al, [r11 + node.len]
    jle .free_lt_file
.free_gt_file:
    ; (i)
    mov rbx, [r11 + node.prev]
    mov rcx, [r11 + node.next]
    mov [rbx + node.next], rcx
    mov [rcx + node.prev], rbx
    ; (ii)
    mov rbx, [r10 + node.prev]
    mov [r11 + node.prev], rbx
    mov [r11 + node.next], r10
    ; (iii)
    mov [rbx + node.next], r11
    mov [r10 + node.prev], r11
    ; update len
    mov al, [r11 + node.len]
    sub [r10 + node.len], al
    ; update ptr
    mov r10, r11
    jmp .find_free
.free_lt_file:
    mov byte [r10 + node.type], TYPE_FILE
    mov ax, [r11 + node.id]
    mov [r10 + node.id], ax
    xor rax, rax
    mov al, [r10 + node.len]
    sub [r11 + node.len], al
    jmp .find_free
.end:
    ret


; rax: result
; r8: position
; r9: node pointer
evaluate:
    xor rax, rax
    xor rbx, rbx
    xor rcx, rcx
    xor rdx, rdx
    xor r8, r8
    lea r9, [ls_head]
.next:
    mov r9, [r9 + node.next]
    mov bl, [r9 + node.type]
    cmp bl, TYPE_TAIL
    je .end
    cmp bl, TYPE_FREE
    je .inc_pos
    ; calc
    mov rbx, r8
    mov cx, [r9 + node.id]
    mov dl, [r9 + node.len]
    shl rbx, 1
    add rbx, rdx
    sub rbx, 1
    imul rbx, rcx
    imul rbx, rdx
    shr rbx, 1
    add rax, rbx
.inc_pos:
    mov dl, [r9 + node.len]
    add r8, rdx
    jmp .next
.end:
    ret



; rax: temp
; rbx: temp pointer
; rcx: temp pointer
; r10: free node pointer
; r11: file node pointer
; r12: ls pointer (unallocated nodes)
compact2:
    xor rax, rax
    mov r11, [ls_tail]
    mov r12, r11
    add r12, node_size
.find_file:
    mov r11, [r11 + node.prev]
    mov al, [r11 + node.type]
    cmp al, TYPE_HEAD
    je .end
    cmp al, TYPE_FILE
    jne .find_file
    ; find_free_init
    lea r10, [ls_head]
.find_free:
    mov r10, [r10 + node.next]
    cmp r10, r11
    je .find_file
    mov al, [r10 + node.type]
    cmp al, TYPE_TAIL
    je .end
    cmp al, TYPE_FREE
    jne .find_free
    ; check
    mov al, [r10 + node.len]
    cmp al, [r11 + node.len]
    jl .find_free
    ; (i)
    mov rbx, [r10 + node.prev]
    mov [r12 + node.prev], rbx
    mov [r12 + node.next], r10
    mov ecx, [r11 + node.type]
    mov [r12 + node.type], ecx ; type & id & len
    ; (ii)
    mov [rbx + node.next], r12
    mov [r10 + node.prev], r12
    add r12, node_size
    ; update len
    mov al, [r11 + node.len]
    sub [r10 + node.len], al
    ; free garbage
    mov byte [r11 + node.type], TYPE_FREE
    mov word [r11 + node.id], 0
    jmp .find_file
.end:
    ret
