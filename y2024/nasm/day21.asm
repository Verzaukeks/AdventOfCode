
section .data
    INPUT:  db "029A", "980A", "179A", "456A", "379A"
    FMT:    db "%lld", 10, 0

    SmallStep:
        %define __ -1
        %define _u  0
        %define _d  1
        %define _l  2
        %define _r  3
        %define _A  4
        db __, _d, __, _A ; up
        db _u, __, _l, _r ; down
        db __, __, __, _d ; left
        db _A, __, _d, __ ; right
        db __, _r, _u, __ ; A
        times 32 db __
    BigStep:
        %define __ -1
        %define _0  0
        %define _1  1
        %define _2  2
        %define _3  3
        %define _4  4
        %define _5  5
        %define _6  6
        %define _7  7
        %define _8  8
        %define _9  9
        %define _A 10
        db _2, __, __, _A ; 0
        db _4, __, __, _2 ; 1
        db _5, _0, _1, _3 ; 2
        db _6, _A, _2, __ ; 3
        db _7, _1, __, _5 ; 4
        db _8, _2, _4, _6 ; 5
        db _9, _3, _5, __ ; 6
        db __, _4, __, _8 ; 7
        db __, _5, _7, _9 ; 8
        db __, _6, _8, __ ; 9
        db _3, __, _0, __ ; A
        times 64 db __

section .bss
    as:     resq 1024
    bs:     resq 1024
    vis:    resq 1024
    vis_end:

section .text
    global main
    extern printf
    default rel


main:
    sub rsp, 32

    call parse

    call a1
    lea rcx, [FMT]
    mov rdx, rax
    xor rax, rax
    call printf

    ; call a2
    ; lea rcx, [FMT]
    ; mov rdx, rax
    ; xor rax, rax
    ; call printf

    add rsp, 32
    xor rax, rax
    ret


parse:
    lea rsi, [INPUT]
    lea rdi, [FMT]
.next:
    cmp byte [rsi], 'A'
    jne .sub0
    add byte [rsi], 10 + '0' - 'A'
.sub0:
    sub byte [rsi], '0'
    add rsi, 1
    cmp rsi, rdi
    jne .next
    ret


a1:
    sub rsp, 8
    mov qword [rsp], 0

%assign i 0
%rep (FMT - INPUT) / 4

    xor r8, r8
    xor r15, r15
    mov rdi, 0b1010_100_100
%assign j 0
%rep 4
    xor rax, rax
    mov al, [INPUT + i * 4 + j]
%if j != 3
    imul r8, 10
    add r8, rax
%endif
    mov rsi, rdi
    mov rdi, rax
    shl rdi, 6
    add rdi, 0b100_100
    call bfs
%assign j j+1
%endrep
    imul r8, r15
    add [rsp], r8

%assign i i+1
%endrep

    mov rax, [rsp]
    add rsp, 8
    ret


bfs:
    lea rax, [vis]
    lea rbx, [vis_end]
.clear:
    mov qword [rax], 0
    add rax, 8
    cmp rax, rbx
    jne .clear

    xor r9, r9
    lea r10, [as]
    lea r11, [bs]
    lea r12, [SmallStep]
    lea r13, [BigStep]
    lea r14, [vis]

    mov [r10], rsi
    add r10, 8

    mov qword [r10], -42
    mov qword [r11], -42
.step:
    lea r10, [as]
    lea r11, [bs]
    add r15, 1

    xor r10, r9
    xor r11, r9
    xor r9, r10
    xor r9, r11

    cmp qword [r10], -42
    je .fail

    mov qword [r11], -42
.next:
    mov rax, [r10]
    add r10, 8

    cmp rax, -42
    je .step
    cmp rax, rdi
    je .end

    mov rbx, rax
    shl rbx, 3
    cmp qword [rbx + r14], 0
    jne .next
    mov [rbx + r14], r15

    call .button_u
    call .button_d
    call .button_l
    call .button_r
    call .button_a

    jmp .next
.button_u:
    mov rcx, 0
    jmp .button_lrud
.button_d:
    mov rcx, 1
    jmp .button_lrud
.button_l:
    mov rcx, 2
    jmp .button_lrud
.button_r:
    mov rcx, 3
.button_lrud:
    mov rbx, rax
    and rbx, 0b111

    shl rbx, 2
    add rbx, rcx
    mov cl, [rbx + r12]
    cmp cl, -1
    je .end

    mov rbx, rax
    and rbx, ~0b111
    add rbx, rcx

    mov [r11], rbx
    add r11, 8
    mov qword [r11], -42

    ret
.button_a:
    mov rcx, rax
    and rcx, 0b111

    cmp rcx, 0b100
    je .button2_a
.button2_udlr:
    mov rbx, rax
    shr rbx, 3
    and rbx, 0b111

    shl rbx,2
    add rbx, rcx
    mov cl, [rbx + r12]
    cmp cl, -1
    je .end

    mov rbx, rax
    and rbx, ~0b111_000
    shl rcx, 3
    add rbx, rcx

    mov [r11], rbx
    add r11, 8
    mov qword [r11], -42

    ret
.button2_a:
    mov rcx, rax
    shr rcx, 3
    and rcx, 0b111

    cmp rcx, 0b100
    je .button3_a
.button3_udlr:
    mov rbx, rax
    shr rbx, 6
    and rbx, 0b1111

    shl rbx, 2
    add rbx, rcx
    mov cl, [rbx + r13]
    cmp cl, -1
    je .end

    mov rbx, rax
    and rbx, ~0b1111_000_000
    shl rcx, 6
    add rbx, rcx

    mov [r11], rbx
    add r11, 8
    mov qword [r11], -42

    ret
.fail:
    mov r15, -23456
.button3_a:
.end:
    ret
