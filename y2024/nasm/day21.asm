; Lo and behold! Troubleshooting was a pain. Who could have known?

section .data
    INPUT:  db "029A", "980A", "179A", "456A", "379A"
    FMT:    db "%lld", 10, 0

    MovesSmall:
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
    MovesBig:
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
    Moves:
        dq MovesSmall
        dq MovesBig

    PositionsSmall:
        db 1, 1 ; up
        db 1, 0 ; down
        db 0, 0 ; left
        db 2, 0 ; right
        db 2, 1 ; A
        times 3 db 0, 0
    PositionsBig:
        db 1, 0 ; 0
        db 0, 1 ; 1
        db 1, 1 ; 2
        db 2, 1 ; 3
        db 0, 2 ; 4
        db 1, 2 ; 5
        db 2, 2 ; 6
        db 0, 3 ; 7
        db 1, 3 ; 8
        db 2, 3 ; 9
        db 2, 0 ; A
        times 5 db 0, 0
    Positions:
        dq PositionsSmall
        dq PositionsBig

section .bss
    cache:  resq 11 * 11 * 26
    cache_end:

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

    call a2
    lea rcx, [FMT]
    mov rdx, rax
    xor rax, rax
    call printf

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
    mov rcx, 2
    jmp a12
a2:
    call clearCache
    mov rcx, 25
    jmp a12


struc a_para
    .depth  resq 1
    .state  resq 1
    .number resq 1
    .length resq 1
    .result resq 1
endstruc
a12:
    sub rsp, a_para_size
    mov [rsp + a_para.depth], rcx
    mov qword [rsp + a_para.result], 0

    sub rsp, dp_para_size

%assign i 0
%rep  (FMT - INPUT) / 4
    mov qword [rsp + dp_para_size + a_para.state], 10
    mov qword [rsp + dp_para_size + a_para.number], 0
    mov qword [rsp + dp_para_size + a_para.length], 0
%rep 4
    xor rbx, rbx
    mov bl, [INPUT + i]

    %if (i % 4) != 3
    mov rax, [rsp + dp_para_size + a_para.number]
    imul rax, 10
    add rax, rbx
    mov [rsp + dp_para_size + a_para.number], rax
    %endif

    mov rax, [rsp + dp_para_size + a_para.state]
    mov      [rsp + dp_para_size + a_para.state], rbx
    mov rcx, [rsp + dp_para_size + a_para.depth]

    mov qword [rsp + dp_para.big], 1
    mov qword [rsp + dp_para.current], rax
    mov qword [rsp + dp_para.goal], rbx
    mov qword [rsp + dp_para.depth], rcx
    call dp
    add [rsp + dp_para_size + a_para.length], rax
%assign i i+1
%endrep
    mov rax, [rsp + dp_para_size + a_para.number]
    imul rax, [rsp + dp_para_size + a_para.length]
    add [rsp + dp_para_size + a_para.result], rax
%endrep

    add rsp, dp_para_size

    mov rax, [rsp + a_para.result]
    add rsp, a_para_size
    ret


struc dp_para
    .big        resq 1
    .current    resq 1
    .goal       resq 1
    .depth      resq 1

    .result     resq 1
    .path       resq 1
    .path_pos   resq 1
    .path_len   resq 1
    .path_udlr  resq 1
    .tmp_res    resq 1
    .tmp_state_self resq 1
    .tmp_state_down resq 1
endstruc
dp:
    ; check cache
    lea rbx, [cache]
    mov rcx, [rsp + 8 + dp_para.current]
    imul rcx, 11
    add rcx, [rsp + 8 + dp_para.goal]
    imul rcx, 26
    add rcx, [rsp + 8 + dp_para.depth]
    imul rcx, 8
    mov rax, [rbx + rcx]
    cmp rax, 0
    jnz .end

    ; initialize
    mov qword [rsp + 8 + dp_para.result], 0x7f7f7f7f
    shl qword [rsp + 8 + dp_para.result], 32
    add qword [rsp + 8 + dp_para.result], 0x7f7f7f7f

    mov qword [rsp + 8 + dp_para.path], 0
    mov qword [rsp + 8 + dp_para.path_pos], 0
    mov qword [rsp + 8 + dp_para.path_len], 0
    mov qword [rsp + 8 + dp_para.path_udlr], 0

.case0:
    cmp qword [rsp + 8 + dp_para.depth], 0
    jne .case1

    xor rax, rax
    lea rsi, [Positions]
    mov rdi, [rsp + 8 + dp_para.big]
    mov rsi, [rsi + rdi * 8]
    mov r8, [rsp + 8 + dp_para.current]
    mov r9, [rsp + 8 + dp_para.goal]

    xor rbx, rbx
    xor rcx, rcx
    mov bl, [rsi + r8 * 2]
    mov cl, [rsi + r9 * 2]
    sub rbx, rcx
    mov rcx, rbx
    sar rcx, 63
    xor rbx, rcx
    sub rbx, rcx
    add rax, rbx

    xor rbx, rbx
    xor rcx, rcx
    mov bl, [rsi + r8 * 2 + 1]
    mov cl, [rsi + r9 * 2 + 1]
    sub rbx, rcx
    mov rcx, rbx
    sar rcx, 63
    xor rbx, rcx
    sub rbx, rcx
    add rax, rbx

    mov [rsp + 8 + dp_para.result], rax
    jmp .write_cache
    
.case1:
    mov rax, [rsp + 8 + dp_para.current]
    cmp rax, [rsp + 8 + dp_para.goal]
    jne .path_right

    mov qword [rsp + 8 + dp_para.result], 0
    jmp .write_cache

.path_right:
    lea rsi, [Positions]
    mov rdi, [rsp + 8 + dp_para.big]
    mov rsi, [rsi + rdi * 8]
    mov r8, [rsp + 8 + dp_para.current]
    mov r9, [rsp + 8 + dp_para.goal]

    xor rbx, rbx
    xor rcx, rcx
    mov cl, [rsi + r9 * 2]
    mov bl, [rsi + r8 * 2]
    sub rcx, rbx
    cmp rcx, 0
    jle .path_left

    mov rbx, 1
    shl rbx, cl
    shl rbx, cl
    sub rbx, 1
    and rbx, (_r << 4) | (_r << 2) | (_r << 0)

    push rcx
    mov rcx, [rsp + 16 + dp_para.path_pos]
    shl rbx, cl
    shl rbx, cl
    pop rcx

    or  [rsp + 8 + dp_para.path], rbx
    add [rsp + 8 + dp_para.path_pos], rcx
    add [rsp + 8 + dp_para.path_len], rcx
    mov [rsp + 8 + dp_para.path_udlr + _r], cl

.path_left:
    xor rbx, rbx
    xor rcx, rcx
    mov cl, [rsi + r8 * 2]
    mov bl, [rsi + r9 * 2]
    sub rcx, rbx
    cmp rcx, 0
    jle .path_down

    mov rbx, 1
    shl rbx, cl
    shl rbx, cl
    sub rbx, 1
    and rbx, (_l << 4) | (_l << 2) | (_l << 0)

    push rcx
    mov rcx, [rsp + 16 + dp_para.path_pos]
    shl rbx, cl
    shl rbx, cl
    pop rcx

    or  [rsp + 8 + dp_para.path], rbx
    add [rsp + 8 + dp_para.path_pos], rcx
    add [rsp + 8 + dp_para.path_len], rcx
    mov [rsp + 8 + dp_para.path_udlr + _l], cl

.path_down:
    xor rbx, rbx
    xor rcx, rcx
    mov cl, [rsi + r8 * 2 + 1]
    mov bl, [rsi + r9 * 2 + 1]
    sub rcx, rbx
    cmp rcx, 0
    jle .path_up

    mov rbx, 1
    shl rbx, cl
    shl rbx, cl
    sub rbx, 1
    and rbx, (_d << 4) | (_d << 2) | (_d << 0)

    push rcx
    mov rcx, [rsp + 16 + dp_para.path_pos]
    shl rbx, cl
    shl rbx, cl
    pop rcx

    or  [rsp + 8 + dp_para.path], rbx
    add [rsp + 8 + dp_para.path_pos], rcx
    add [rsp + 8 + dp_para.path_len], rcx
    mov [rsp + 8 + dp_para.path_udlr + _d], cl

.path_up:
    xor rbx, rbx
    xor rcx, rcx
    mov cl, [rsi + r9 * 2 + 1]
    mov bl, [rsi + r8 * 2 + 1]
    sub rcx, rbx
    cmp rcx, 0
    jle .walk

    mov rbx, 1
    shl rbx, cl
    shl rbx, cl
    sub rbx, 1
    and rbx, (_u << 4) | (_u << 2) | (_u << 0)

    push rcx
    mov rcx, [rsp + 16 + dp_para.path_pos]
    shl rbx, cl
    shl rbx, cl
    pop rcx

    or  [rsp + 8 + dp_para.path], rbx
    add [rsp + 8 + dp_para.path_pos], rcx
    add [rsp + 8 + dp_para.path_len], rcx
    mov [rsp + 8 + dp_para.path_udlr + _u], cl

.walk:
    mov qword [rsp + 8 + dp_para.path_pos], 0
    mov qword [rsp + 8 + dp_para.tmp_res], 0
    mov qword [rsp + 8 + dp_para.tmp_state_down], 4
    mov rax, [rsp + 8 + dp_para.current]
    mov [rsp + 8 + dp_para.tmp_state_self], rax
.walk_next:
    lea rsi, [Moves]
    mov rdi, [rsp + 8 + dp_para.big]
    mov rsi, [rsi + rdi * 8]

    mov rbx, [rsp + 8 + dp_para.path]
    mov rcx, [rsp + 8 + dp_para.path_pos]
    shr rbx, cl
    shr rbx, cl
    and rbx, 0b11
    add rsi, rbx

    mov rax, [rsp + 8 + dp_para.tmp_state_self]
    mov al, [rsi + rax * 4]
    cmp al, -1
    je .walk_inc
    mov [rsp + 8 + dp_para.tmp_state_self], rax

    mov rax, [rsp + 8 + dp_para.tmp_state_down]
    mov [rsp + 8 + dp_para.tmp_state_down], rbx

    mov rcx, [rsp + 8 + dp_para.depth]
    sub rcx, 1
    
    sub rsp, dp_para_size
    mov qword [rsp + dp_para.big], 0
    mov qword [rsp + dp_para.current], rax
    mov qword [rsp + dp_para.goal], rbx
    mov qword [rsp + dp_para.depth], rcx
    call dp
    add rsp, dp_para_size

    add [rsp + 8 + dp_para.tmp_res], rax

    mov rax, [rsp + 8 + dp_para.path_pos]
    add rax, 1
    mov [rsp + 8 + dp_para.path_pos], rax
    cmp rax, [rsp + 8 + dp_para.path_len]
    jne .walk_next

.walk_end:
    mov rax, [rsp + 8 + dp_para.tmp_state_down]
    mov rcx, [rsp + 8 + dp_para.depth]
    sub rcx, 1

    sub rsp, dp_para_size
    mov qword [rsp + dp_para.big], 0
    mov qword [rsp + dp_para.current], rax
    mov qword [rsp + dp_para.goal], 4
    mov qword [rsp + dp_para.depth], rcx
    call dp
    add rsp, dp_para_size

    add [rsp + 8 + dp_para.tmp_res], rax

    mov rax, [rsp + 8 + dp_para.tmp_res]
    cmp rax, [rsp + 8 + dp_para.result]
    jge .walk_inc
    mov [rsp + 8 + dp_para.result], rax
.walk_inc:
    call next_path
    cmp rax, 0
    jne .walk

    ; a button presses
    mov rax, [rsp + 8 + dp_para.path_len]
    add [rsp + 8 + dp_para.result], rax

.write_cache:
    lea rbx, [cache]
    mov rcx, [rsp + 8 + dp_para.current]
    imul rcx, 11
    add rcx, [rsp + 8 + dp_para.goal]
    imul rcx, 26
    add rcx, [rsp + 8 + dp_para.depth]
    imul rcx, 8
    mov rax, [rsp + 8 + dp_para.result]
    add rax, [rsp + 8 + dp_para.big] ; press final button
    mov [rbx + rcx], rax
.end:
    ret


next_path:
    sub rsp, 8
    mov rax, [rsp + 24 + dp_para.path]
    mov rcx, [rsp + 24 + dp_para.path_len]
    mov rbx, 1
    shl rbx, cl
    shl rbx, cl
.next:
    add rax, 1
    cmp rax, rbx
    je .fail

    mov r8, rcx
    mov r9, rax
    mov qword [rsp], 0
.step:
    mov rdx, r9
    and rdx, 0b11
    add byte [rsp + rdx], 1

    sub r8, 1
    shr r9, 2

    cmp r8, 0
    jne .step

    mov rdx, [rsp]
    cmp rdx, [rsp + 24 + dp_para.path_udlr]
    jne .next
.success:
    mov [rsp + 24 + dp_para.path], rax
    mov rax, 1
    add rsp, 8
    ret
.fail:
    xor rax, rax
    add rsp, 8
    ret


clearCache:
    lea rsi, [cache]
    lea rdi, [cache_end]
.next:
    mov qword [rsi], 0
    add rsi, 8
    cmp rsi, rdi
    jl .next
    ret
