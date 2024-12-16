%define SIZE 141

struc pq_node
    .score  resq 1
    .pos    resq 1
    .dir    resq 1
endstruc

section .data
    INPUT:  incbin "../inputs/d16"
            db 0
    FMT:    db "%lld", 10, 0

section .bss
    pq_size: resq 1
    pq_data: resb 1_000_000 * pq_node_size
    visited: resq SIZE * (SIZE + 2) * 4

section .text
    global main
    extern printf
    default rel


pq_add:
    mov rax, [rsp + 8 + pq_node.score]
    lea r8,  [pq_data]
    mov rsi, [pq_size]
    xor rdi, rdi

    mov  rbx, rsi
    imul rbx, pq_node_size
    add  rbx, r8 

    mov r10, [rsp + 8 + pq_node.score]
    mov r11, [rsp + 8 + pq_node.pos  ]
    mov r12, [rsp + 8 + pq_node.dir  ]
    mov [rbx + pq_node.score], r10
    mov [rbx + pq_node.pos  ], r11
    mov [rbx + pq_node.dir  ], r12
    add qword [pq_size], 1

    cmp rsi, 0
    jz .end
.up:
    mov rdi, rsi
    sub rdi, 1
    shr rdi, 1

    mov  rcx, rdi
    imul rcx, pq_node_size
    add  rcx, r8

    cmp rax, [rcx + pq_node.score]
    jge .end

    mov r10, [rbx + pq_node.score]
    mov r11, [rbx + pq_node.pos  ]
    mov r12, [rbx + pq_node.dir  ]
    mov r13, [rcx + pq_node.score]
    mov r14, [rcx + pq_node.pos  ]
    mov r15, [rcx + pq_node.dir  ]
    mov [rcx + pq_node.score], r10
    mov [rcx + pq_node.pos  ], r11
    mov [rcx + pq_node.dir  ], r12
    mov [rbx + pq_node.score], r13
    mov [rbx + pq_node.pos  ], r14
    mov [rbx + pq_node.dir  ], r15

    mov rbx, rcx
    mov rsi, rdi
    cmp rsi, 0
    jne .up
.end:
    ret


pq_take:
    mov r10, [pq_data + pq_node.score]
    mov r11, [pq_data + pq_node.pos  ]
    mov r12, [pq_data + pq_node.dir  ]
    mov [rsp + 8 + pq_node.score], r10
    mov [rsp + 8 + pq_node.pos  ], r11
    mov [rsp + 8 + pq_node.dir  ], r12

    sub qword [pq_size], 1
    cmp qword [pq_size], 0
    jz .end

    lea r8,  [pq_data]

    mov  rbx, [pq_size]
    imul rbx, pq_node_size
    add  rbx, r8

    mov rax, [rbx + pq_node.score]
    mov r11, [rbx + pq_node.pos  ]
    mov r12, [rbx + pq_node.dir  ]
    mov [pq_data + pq_node.score], rax
    mov [pq_data + pq_node.pos  ], r11
    mov [pq_data + pq_node.dir  ], r12

    lea rbx, [pq_data]
    xor rsi, rsi
    xor rdi, rdi
    mov r9,  [pq_size]
.down:
    mov rdi, rsi
    shl rdi, 1
    add rdi, 1

    mov  rcx, rdi
    imul rcx, pq_node_size
    add  rcx, r8

    cmp rdi, r9
    jge .end

    mov rdx, rdi
    add rdx, 1
    cmp rdx, r9
    jge .check

    mov rdx, [rcx + pq_node.score]
    cmp rdx, [rcx + pq_node.score + pq_node_size]
    jle .check

    add rdi, 1
    add rcx, pq_node_size
.check:
    cmp rax, [rcx + pq_node.score]
    jle .end

    mov r10, [rbx + pq_node.score]
    mov r11, [rbx + pq_node.pos  ]
    mov r12, [rbx + pq_node.dir  ]
    mov r13, [rcx + pq_node.score]
    mov r14, [rcx + pq_node.pos  ]
    mov r15, [rcx + pq_node.dir  ]
    mov [rcx + pq_node.score], r10
    mov [rcx + pq_node.pos  ], r11
    mov [rcx + pq_node.dir  ], r12
    mov [rbx + pq_node.score], r13
    mov [rbx + pq_node.pos  ], r14
    mov [rbx + pq_node.dir  ], r15

    mov rbx, rcx
    mov rsi, rdi
    jmp .down
.end:
    ret


main:
    sub rsp, 32

    call a1
    mov r13, [rsp - 8 - pq_node_size + pq_node.score]
    mov r14, [rsp - 8 - pq_node_size + pq_node.pos]
    mov r15, [rsp - 8 - pq_node_size + pq_node.dir]
    lea rcx, [FMT]
    mov rdx, rax
    xor rax, rax
    call printf

    call a2
    lea rcx, [FMT]
    mov rdx, rax
    xor rax, rax
    call printf

    add rsp, 32
    xor rax, rax
    ret


a1:
    xor rax, rax
    lea rbx, [INPUT]
    sub rsp, pq_node_size
.find_S:
    add rbx, 1
    mov al, [rbx]
    cmp al, 'S'
    jne .find_S

    mov qword [rsp + pq_node.score], 1
    mov qword [rsp + pq_node.pos  ], rbx
    mov qword [rsp + pq_node.dir  ], 1
    call pq_add
.run:
    call pq_take

    mov rax, [rsp + pq_node.pos]
    lea rbx, [INPUT]
    sub rax, rbx
    shl rax, 2
    cmp qword [rsp + pq_node.dir], 1
    je .i3
    cmp qword [rsp + pq_node.dir], -1
    je .i2
    cmp qword [rsp + pq_node.dir], SIZE + 2
    je .i1
    cmp qword [rsp + pq_node.dir], -SIZE - 2
    je .i0
    jmp .run
.i3: add rax, 1
.i2: add rax, 1
.i1: add rax, 1
.i0:
    shl rax, 3
    lea rbx, [visited]
    cmp qword [rax + rbx], 0
    jnz .run
    mov rcx, [rsp + pq_node.score]
    mov [rax + rbx], rcx

    mov rbx, [rsp + pq_node.pos]
    mov al, [rbx]
    cmp al, 'E'
    je .end

    add rbx, [rsp + pq_node.dir]
    mov al, [rbx]
    cmp al, '#'
    je .lr

    add qword [rsp + pq_node.score], 1
    mov qword [rsp + pq_node.pos  ], rbx
    call pq_add

    sub qword [rsp + pq_node.score], 1
    mov rbx, [rsp + pq_node.pos]
    sub rbx, [rsp + pq_node.dir]
    mov [rsp + pq_node.pos], rbx
.lr:
    add qword [rsp + pq_node.score], 1000

    cmp qword [rsp + pq_node.dir], 1
    je .lr_hor
    cmp qword [rsp + pq_node.dir], -1
    je .lr_hor

    mov qword [rsp + pq_node.dir], 1
    call pq_add
    mov qword [rsp + pq_node.dir], -1
    call pq_add
    jmp .run
.lr_hor:
    mov qword [rsp + pq_node.dir], SIZE + 2
    call pq_add
    mov qword [rsp + pq_node.dir], -SIZE - 2
    call pq_add
    jmp .run
.end:
    mov rax, [rsp + pq_node.score]
    sub rax, 1
    add rsp, pq_node_size
    ret


a2:
    mov qword [pq_size], 0
    sub rsp, pq_node_size
    mov [rsp + pq_node.score], r13
    mov [rsp + pq_node.pos  ], r14
    mov [rsp + pq_node.dir  ], r15
    call pq_add
.run:
    cmp qword [pq_size], 0
    jz .count

    call pq_take

    mov rax, [rsp + pq_node.pos]
    lea rbx, [INPUT]
    sub rax, rbx
    shl rax, 2
    cmp qword [rsp + pq_node.dir], 1
    je .i3
    cmp qword [rsp + pq_node.dir], -1
    je .i2
    cmp qword [rsp + pq_node.dir], SIZE + 2
    je .i1
    cmp qword [rsp + pq_node.dir], -SIZE - 2
    je .i0
    jmp .run
.i3: add rax, 1
.i2: add rax, 1
.i1: add rax, 1
.i0:
    shl rax, 3
    lea rbx, [visited]
    mov rcx, [rsp + pq_node.score]
    cmp qword [rax + rbx], rcx
    jne .run

    mov rbx, [rsp + pq_node.pos]
    cmp byte [rbx], 'S'
    je .run
    mov byte [rbx], 'O'

    sub qword [rsp + pq_node.score], 1
    mov rax, [rsp + pq_node.dir]
    sub [rsp + pq_node.pos], rax
    call pq_add

    sub qword [rsp + pq_node.score], 999
    mov rax, [rsp + pq_node.dir]
    add [rsp + pq_node.pos], rax

    cmp qword [rsp + pq_node.dir], 1
    je .lr_hor
    cmp qword [rsp + pq_node.dir], -1
    je .lr_hor

    mov qword [rsp + pq_node.dir], 1
    call pq_add
    mov qword [rsp + pq_node.dir], -1
    call pq_add
    jmp .run
.lr_hor:
    mov qword [rsp + pq_node.dir], SIZE + 2
    call pq_add
    mov qword [rsp + pq_node.dir], -SIZE - 2
    call pq_add
    jmp .run
.count:
    mov rax, 1
    lea rbx, [INPUT]
.count_next:
    add rbx, 1
    cmp byte [rbx], 0
    jz .end
    cmp byte [rbx], 'O'
    jne .count_next

    add rax, 1
    jmp .count_next
.end:
    add rsp, pq_node_size
    ret