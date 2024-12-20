%define SIZE 141
%define THRESHOLD 100
%define GAP 20

section .data
    INPUT:  incbin "../inputs/d20"
            times SIZE + 2 db 0
    FMT:    db "%lld", 10, 0

section .bss
    DIS:    resq SIZE * (SIZE + 2)

section .text
    global main
    extern printf
    default rel


main:
    sub rsp, 32

    call a1
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
    xor r8, r8
    xor r9, r9
    lea r10, [INPUT]
    lea r11, [DIS]
.find_S:
    add r10, 1
    add r11, 8
    cmp byte [r10], 'S'
    jne .find_S

    add r9, 1
    mov byte [r10], '.'
    mov qword [r11], r9
.t:
    add r9, 1
    cmp byte [r10], 'E'
    je .check
.t0:
    cmp byte [r10 + 1], '#'
    je .t1
    cmp qword [r11 + 8], 0
    jne .t1

    add r10, 1
    add r11, 8
    mov [r11], r9
    jmp .t
.t1:
    cmp byte [r10 - 1], '#'
    je .t2
    cmp qword [r11 - 8], 0
    jne .t2

    add r10, -1
    add r11, -8
    mov [r11], r9
    jmp .t
.t2:
    cmp byte [r10 + SIZE + 2], '#'
    je .t3
    cmp qword [r11 + (SIZE + 2) * 8], 0
    jne .t3

    add r10, SIZE + 2
    add r11, (SIZE + 2) * 8
    mov [r11], r9
    jmp .t
.t3:
    cmp byte [r10 - SIZE - 2], '#'
    je .end
    cmp qword [r11 - (SIZE + 2) * 8], 0
    jne .end

    add r10, -SIZE - 2
    add r11, -(SIZE + 2) * 8
    mov [r11], r9
    jmp .t
.check:
    mov byte [r10], '.'

    lea r10, [INPUT + (SIZE + 2)]
    lea r11, [DIS + (SIZE + 2) * 8]
.next:
    add r10, 1
    add r11, 8
    cmp byte [r10], 0
    je .end
    cmp byte [r10], '#'
    jne .next
.hor:
    cmp byte [r10 - 1], '.'
    jne .ver
    cmp byte [r10 + 1], '.'
    jne .ver

    mov rax, [r11 + 8]
    sub rax, [r11 - 8]
    mov rbx, rax
    sar rbx, 63
    xor rax, rbx
    sub rax, rbx
    sub rax, 2

    cmp rax, THRESHOLD
    jl .next

    add r8, 1
    jmp .next
.ver:
    cmp byte [r10 - SIZE - 2], '.'
    jne .next
    cmp byte [r10 + SIZE + 2], '.'
    jne .next

    mov rax, [r11 + (SIZE + 2) * 8]
    sub rax, [r11 - (SIZE + 2) * 8]
    mov rbx, rax
    sar rbx, 63
    xor rax, rbx
    sub rax, rbx
    sub rax, 2

    cmp rax, THRESHOLD
    jl .next

    add r8, 1
    jmp .next
.end:
    mov rax, r8
    ret


a2:
    xor r8, r8
    xor r10, r10
    xor r11, r11
    lea r12, [INPUT]
    lea r13, [DIS]
.next_y:
    xor r10, r10
    add r11, 1
.next_x:
    add r10, 1

    mov rbx, r11
    imul rbx, SIZE + 2
    add rbx, r10
    add rbx, r12

    cmp byte [rbx], 0
    je .end
    cmp byte [rbx], 10
    je .next_y
    cmp byte [rbx], '.'
    jne .next_x

    sub rbx, r12
    imul rbx, 8
    add rbx, r13
    mov rbx, [rbx]

%assign y -GAP
%rep 2*GAP + 1
    %assign x -GAP
    %rep 2*GAP + 1
        %if x < 0
            %assign abs_x -x
        %else
            %assign abs_x x
        %endif
        %if y < 0
            %assign abs_y -y
        %else
            %assign abs_y y
        %endif
        %if abs_x + abs_y <= GAP
    
    mov rax, x
    mov rcx, y
    mov rdx, abs_x + abs_y
    call verify

        %endif
    %assign x x+1
    %endrep
%assign y y+1
%endrep

    jmp .next_x
.end:
    mov rax, r8
    shr rax, 1
    ret


verify:
    add rax, r10
    cmp rax, 0
    jl .end
    cmp rax, SIZE
    jge .end

    add rcx, r11
    cmp rcx, 0
    jl .end
    cmp rcx, SIZE
    jge .end

    imul rcx, SIZE + 2
    add rcx, rax
    imul rcx, 8
    add rcx, r13
    mov rcx, [rcx]
    cmp rcx, 0
    je .end

    sub rcx, rbx
    mov rax, rcx
    sar rax, 63
    xor rcx, rax
    sub rcx, rax
    sub rcx, rdx

    cmp rcx, THRESHOLD
    jl .end

    add r8, 1
.end:
    ret
