
section .data
    INPUT:  incbin "../inputs/d03"
            db 0
    FMT:    db "%d", 10, 0

%macro cond 3
    db %1 ; char
    dq %2 ; next_state
    dq %3 ; call_function
%endmacro
%macro cond 2
    db %1
    dq %2
    dq dummy
%endmacro
%macro cond 1
    db 0
    dq %1
    dq dummy
%endmacro

    a1_sm_m:
        cond 'm', a1_sm_u
        cond a1_sm_m
    a1_sm_u:
        cond 'm', a1_sm_u
        cond 'u', a1_sm_l
        cond a1_sm_m
    a1_sm_l:
        cond 'm', a1_sm_u
        cond 'l', a1_sm_open
        cond a1_sm_m
    a1_sm_open:
        cond 'm', a1_sm_u
        cond '(', a1_sm_x, reset_xy
        cond a1_sm_m
    a1_sm_x:
        cond 'm', a1_sm_u
        cond '0', a1_sm_x, add_x
        cond '1', a1_sm_x, add_x
        cond '2', a1_sm_x, add_x
        cond '3', a1_sm_x, add_x
        cond '4', a1_sm_x, add_x
        cond '5', a1_sm_x, add_x
        cond '6', a1_sm_x, add_x
        cond '7', a1_sm_x, add_x
        cond '8', a1_sm_x, add_x
        cond '9', a1_sm_x, add_x
        cond ',', a1_sm_y
        cond a1_sm_m
    a1_sm_y:
        cond 'm', a1_sm_u
        cond '0', a1_sm_y, add_y
        cond '1', a1_sm_y, add_y
        cond '2', a1_sm_y, add_y
        cond '3', a1_sm_y, add_y
        cond '4', a1_sm_y, add_y
        cond '5', a1_sm_y, add_y
        cond '6', a1_sm_y, add_y
        cond '7', a1_sm_y, add_y
        cond '8', a1_sm_y, add_y
        cond '9', a1_sm_y, add_y
        cond ')', a1_sm_m, mul_xy
        cond a1_sm_m

    a2_sm_md:
        cond 'm', a2_sm_u
        cond 'd', a2_sm_o
        cond a2_sm_md
    a2_sm_u:
        cond 'm', a2_sm_u
        cond 'd', a2_sm_o
        cond 'u', a2_sm_l
        cond a2_sm_md
    a2_sm_l:
        cond 'm', a2_sm_u
        cond 'd', a2_sm_o
        cond 'l', a2_sm_open
        cond a2_sm_md
    a2_sm_open:
        cond 'm', a2_sm_u
        cond 'd', a2_sm_o
        cond '(', a2_sm_x, reset_xy
        cond a2_sm_md
    a2_sm_x:
        cond 'm', a2_sm_u
        cond 'd', a2_sm_o
        cond '0', a2_sm_x, add_x
        cond '1', a2_sm_x, add_x
        cond '2', a2_sm_x, add_x
        cond '3', a2_sm_x, add_x
        cond '4', a2_sm_x, add_x
        cond '5', a2_sm_x, add_x
        cond '6', a2_sm_x, add_x
        cond '7', a2_sm_x, add_x
        cond '8', a2_sm_x, add_x
        cond '9', a2_sm_x, add_x
        cond ',', a2_sm_y
        cond a2_sm_md
    a2_sm_y:
        cond 'm', a2_sm_u
        cond 'd', a2_sm_o
        cond '0', a2_sm_y, add_y
        cond '1', a2_sm_y, add_y
        cond '2', a2_sm_y, add_y
        cond '3', a2_sm_y, add_y
        cond '4', a2_sm_y, add_y
        cond '5', a2_sm_y, add_y
        cond '6', a2_sm_y, add_y
        cond '7', a2_sm_y, add_y
        cond '8', a2_sm_y, add_y
        cond '9', a2_sm_y, add_y
        cond ')', a2_sm_md, mul_xy
        cond a2_sm_md
    a2_sm_o:
        cond 'm', a2_sm_u
        cond 'd', a2_sm_o
        cond 'o', a2_sm_nopen
        cond a2_sm_md
    a2_sm_nopen:
        cond 'm', a2_sm_u
        cond 'd', a2_sm_o
        cond 'n', a2_sm_apostroph
        cond '(', a2_sm_close
        cond a2_sm_md
    a2_sm_close:
        cond 'm', a2_sm_u
        cond 'd', a2_sm_o
        cond ')', a2_sm_md, flag_enable
        cond a2_sm_md
    a2_sm_apostroph:
        cond 'm', a2_sm_u
        cond 'd', a2_sm_o
        cond "'", a2_sm_t
        cond a2_sm_md
    a2_sm_t:
        cond 'm', a2_sm_u
        cond 'd', a2_sm_o
        cond 't', a2_sm_open2
        cond a2_sm_md
    a2_sm_open2:
        cond 'm', a2_sm_u
        cond 'd', a2_sm_o
        cond '(', a2_sm_close2
        cond a2_sm_md
    a2_sm_close2:
        cond 'm', a2_sm_u
        cond 'd', a2_sm_o
        cond ')', a2_sm_md, flag_disable
        cond a2_sm_md


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


; rax: current char
; rbx: current state (parameter)
; rcx: input pointer (parameter)
; rdx: state condition char
; r8: result
; r9: enable flag
; r10: number x
; r11: number y
state_machine:
    mov r8, 0
    mov r9, 1
state_machine_next:
    xor rax, rax
    mov al, [rcx]
    add rcx, 1
    cmp al, 0
    jz state_machine_end
state_machine_find:
    xor rdx, rdx
    mov dl, [rbx]
    cmp dl, 0
    jz state_machine_hit
    cmp dl, al
    je state_machine_hit
    add rbx, 17
    jmp state_machine_find
state_machine_hit:
    call [rbx + 9]
    mov rbx, [rbx + 1]
    jmp state_machine_next
state_machine_end:
    ret


dummy:
    ret
flag_enable:
    mov r9, 1
    ret
flag_disable:
    mov r9, 0
    ret
reset_xy:
    mov r10, 0
    mov r11, 0
    ret
add_x:
    sub rax, '0'
    imul r10, 10
    add r10, rax
    ret
add_y:
    sub rax, '0'
    imul r11, 10
    add r11, rax
    ret
mul_xy:
    cmp r9, 0
    jz dummy
    imul r10, r11
    add r8, r10
    ret


a1:
    lea rbx, [a1_sm_m]
    lea rcx, [INPUT]
    call state_machine
    mov rax, r8
    ret


a2:
    lea rbx, [a2_sm_md]
    lea rcx, [INPUT]
    call state_machine
    mov rax, r8
    ret
