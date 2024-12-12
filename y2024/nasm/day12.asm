%define SIZE 140

struc node
    .parent resq 1
    .area   resq 1
    .peri   resq 1
endstruc

section .data
            dq -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
    INPUT:  incbin "../inputs/d12"
            dq 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    FMT:    db "%lld", 10, 0

section .bss
    ns:     resb (SIZE * SIZE) * node_size
    magic:  resb 64

section .text
    global main
    extern printf
    default rel


main:
    sub rsp, 40

    mov byte [magic +  0], 0b1111
    mov byte [magic +  1], 0b1100
    mov byte [magic +  2], 0b1010
    mov byte [magic +  4], 0b1111
    mov byte [magic +  7], 0b1001
    mov byte [magic +  8], 0b1111
    mov byte [magic + 12], 0b1111
    mov byte [magic + 16], 0b0101
    mov byte [magic + 25], 0b0110
    mov byte [magic + 32], 0b0011
    mov byte [magic + 42], 0b0110
    mov byte [magic + 52], 0b1001

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


reset:
    mov rax, 0
    lea rbx, [ns]
.next:
    mov rcx, rax
    imul rcx, node_size

    mov qword [rbx + rcx + node.parent], rax
    mov qword [rbx + rcx + node.area], 1
    mov qword [rbx + rcx + node.peri], 0

    add rax, 1
    cmp rax, SIZE * SIZE
    jl .next
.end:
    ret


calc:
    xor rax, rax
    xor rbx, rbx
    lea rcx, [ns]
.next:
    mov rdx, rbx
    imul rdx, node_size

    mov rsi, [rcx + rdx + node.parent]
    cmp rsi, rbx
    jne .skip

    mov rsi, [rcx + rdx + node.area]
    mov rdi, [rcx + rdx + node.peri]
    imul rsi, rdi
    add rax, rsi
.skip:
    add rbx, 1
    cmp rbx, SIZE * SIZE
    jl .next
.end:
    ret


ufind:
    lea rbx, [ns]
    mov rcx, [rsp + 8]
    imul rcx, node_size
    mov rax, [rbx + rcx + node.parent]

    cmp rax, [rsp + 8]
    je .end

    push rax
    call ufind
    add rsp, 8

    mov rcx, [rsp + 8]
    imul rcx, node_size
    mov [rbx + rcx + node.parent], rax
.end:
    ret


union:
    sub rsp, 8

    mov rax, [rsp + 16]
    mov [rsp], rax
    call ufind
    mov [rsp + 16], rax

    mov rax, [rsp + 24]
    mov [rsp], rax
    call ufind
    mov [rsp + 24], rax

    add rsp, 8
    cmp rax, [rsp + 8]
    je .fail
    
    lea rax, [ns]
    mov r8, [rsp + 8]
    mov r9, [rsp + 16]
    imul r8, node_size
    imul r9, node_size
    add r8, rax
    add r9, rax

    mov rax, [rsp + 8]
    mov [r9 + node.parent], rax
    mov rax, [r9 + node.area]
    mov rbx, [r9 + node.peri]
    add [r8 + node.area], rax
    add [r8 + node.peri], rbx

    mov rax, 1
    ret
.fail:
    xor rax, rax
    ret


ptr_to_node:
    lea rbx, [INPUT]
    sub rax, rbx
    cmp rax, 0
    jl .fail
    cmp rax, SIZE * (SIZE + 2)
    jge .fail

    xor rdx, rdx
    mov rbx, SIZE + 2
    div rbx
    
    cmp rdx, SIZE
    jge .fail

    mov rbx, rax
    imul rbx, SIZE + 2
    add rbx, rdx
    sub rbx, rax
    sub rbx, rax

    mov rax, 1
    ret
.fail:
    xor rax, rax
    xor rbx, rbx
    ret


bind:
    mov rax, [rsp + 16]
    call ptr_to_node
    cmp rax, 0
    jz .end
    mov r8, rbx

    mov rax, [rsp + 8]
    call ptr_to_node
    cmp rax, 0
    jz .perimeter
    mov r9, rbx

    mov rax, [rsp + 16]
    mov rbx, [rsp + 8]
    xor rcx, rcx
    xor rdx, rdx
    mov cl, [rax]
    mov dl, [rbx]
    cmp cl, dl
    je .combine
.perimeter:
    push r8
    call ufind
    pop r8

    lea rbx, [ns]
    imul rax, node_size
    add qword [rax + rbx + node.peri], 1
    ret
.combine:
    push r8
    push r9
    call union
    add rsp, 16
.end:
    ret


a1:
    call reset
    lea rax, [INPUT]
    push rax
    push rax
.next:
    mov rax, [rsp + 8]
    add rax, 1
    mov [rsp], rax
    call bind

    mov rax, [rsp + 8]
    sub rax, 1
    mov [rsp], rax
    call bind

    mov rax, [rsp + 8]
    add rax, SIZE + 2
    mov [rsp], rax
    call bind

    mov rax, [rsp + 8]
    sub rax, SIZE + 2
    mov [rsp], rax
    call bind

    add qword [rsp + 8], 1
    mov rax, [rsp + 8]
    xor rbx, rbx
    mov bl, [rax]
    cmp bl, 0
    jne .next

    add rsp, 16
    call calc
    ret


a2:
    call reset_peri
    lea rax, [INPUT - SIZE - 4]
.next:
    add rax, 1
    mov bl, [rax]
    cmp bl, 0
    je .end
    push rax
    call check
    pop rax
    jmp .next
.end:
    call calc
    ret


reset_peri:
    mov rax, 0
    lea rbx, [ns]
.next:
    mov rcx, rax
    imul rcx, node_size

    mov qword [rbx + rcx + node.peri], 0

    add rax, 1
    cmp rax, SIZE * SIZE
    jl .next
.end:
    ret


check:
    xor r8, r8
    xor r10, r10
    xor r11, r11
    xor r12, r12
    xor r13, r13

    mov r10b, [rax]
    mov r11b, [rax + 1]
    mov r12b, [rax + SIZE + 2]
    mov r13b, [rax + SIZE + 3]

    xor r9, r9
    cmp r10, r11
    sete r9b
    add r8, r9

    xor r9, r9
    cmp r10, r12
    sete r9b
    shl r9, 1
    add r8, r9

    xor r9, r9
    cmp r11, r12
    sete r9b
    shl r9, 2
    add r8, r9

    xor r9, r9
    cmp r10, r13
    sete r9b
    shl r9, 3
    add r8, r9

    xor r9, r9
    cmp r11, r13
    sete r9b
    shl r9, 4
    add r8, r9

    xor r9, r9
    cmp r12, r13
    sete r9b
    shl r9, 5
    add r8, r9

    xor rax, rax
    lea rbx, [magic]
    mov al, [rbx + r8]
    push rax
.c0:
    mov rax, [rsp]
    and rax, 0b0001
    cmp rax, 0
    jz .c1
    mov rax, [rsp + 16]
    call edge
.c1:
    mov rax, [rsp]
    and rax, 0b0010
    cmp rax, 0
    jz .c2
    mov rax, [rsp + 16]
    add rax, 1
    call edge
.c2:
    mov rax, [rsp]
    and rax, 0b0100
    cmp rax, 0
    jz .c3
    mov rax, [rsp + 16]
    add rax, SIZE + 2
    call edge
.c3:
    mov rax, [rsp]
    and rax, 0b1000
    cmp rax, 0
    jz .end
    mov rax, [rsp + 16]
    add rax, SIZE + 3
    call edge
.end:
    pop rax
    ret


edge:
    push rax
    call ptr_to_node
    add rsp, 8

    cmp rax, 0
    jz .end

    push rbx
    call ufind
    add rsp, 8    

    imul rax, node_size
    lea rbx, [ns]
    add qword [rax + rbx + node.peri], 1
.end:
    ret