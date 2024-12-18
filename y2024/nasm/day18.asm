%define SIZE 71
%define FALL 1024

section .data
    INPUT:  incbin "../inputs/d18"
            db 0
    FMT:    db "%lld", 10, 0
    FMT2:   db "%lld,%lld", 10, 0

section .bss
    map:    resb SIZE * SIZE
    ns:     resq 10000
    as:     resq 10000
    bs:     resq 10000

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
    lea rcx, [FMT2]
    mov rdx, rax
    mov r8, rbx
    xor rax, rax
    call printf

    add rsp, 32
    xor rax, rax
    ret


; rax: current char
; rbx: current number
; r8: input pointer
; r9: nums pointer
parse:
    xor rax, rax
    xor rbx, rbx
    lea r8, [INPUT]
    lea r9, [ns]
.empty:
    mov al, [r8]
    add r8, 1
    cmp rax, 0
    jz .end
    cmp rax, '0'
    jl .empty
    cmp rax, '9'
    jg .empty
    sub r8, 1
.number:
    mov al, [r8]
    add r8, 1
    cmp rax, '0'
    jl .add
    cmp rax, '9'
    jg .add
    sub rax, '0'
    imul rbx, 10
    add rbx, rax
    jmp .number
.add:
    mov [r9], rbx
    add r9, 8
    xor rbx, rbx
    jmp .empty
.end:
    mov qword [r9], -1
    ret


a1:
    lea r8, [ns]
    mov r9, r8
    add r9, 16 * FALL
.fall:
    lea rax, [map]
    add rax, [r8]
    mov rbx, [r8 + 8]
    imul rbx, SIZE
    
    mov byte [rax + rbx], '#'

    add r8, 16
    cmp r8, r9
    jl .fall

    call find
    ret


find:
    xor r8, r8
    xor r9, r9
    lea r10, [as]
    lea r11, [bs]
    lea r12, [map]
    
    mov qword [r10], 0
    mov qword [r10 + 8], 0
    add r10, 16

    mov qword [r10], -42
    mov qword [r11], -42
.step:
    add r8, 1
    lea r10, [as]
    lea r11, [bs]

    xor r10, r9
    xor r11, r9
    xor r9, r10
    xor r9, r11

    cmp qword [r10], -42
    je .fail

    mov qword [r11], -42
.next:
    mov rax, [r10]
    mov rbx, [r10 + 8]
    add r10, 16
    cmp rax, -42
    je .step

    cmp rax, 0
    jl .next
    cmp rbx, 0
    jl .next
    cmp rax, SIZE
    jge .next
    cmp rbx, SIZE
    jge .next

    mov rcx, rbx
    imul rcx, SIZE
    add rcx, rax
    cmp byte [r12 + rcx], 0
    jnz .next
    mov byte [r12 + rcx], 'O'

    cmp rcx, SIZE * SIZE - 1
    je .end

    add rax, 1
    mov [r11 + 0], rax
    mov [r11 + 8], rbx
    add r11, 16
    
    add rax, -2
    mov [r11 + 0], rax
    mov [r11 + 8], rbx
    add r11, 16
    
    add rax, 1
    add rbx, 1
    mov [r11 + 0], rax
    mov [r11 + 8], rbx
    add r11, 16
    
    add rbx, -2
    mov [r11 + 0], rax
    mov [r11 + 8], rbx
    add r11, 16

    mov qword [r11], -42
    jmp .next
.end:
    mov rax, r8
    sub rax, 1
    ret
.fail:
    mov rax, -42
    ret


a2:
    lea r13, [ns]
    add r13, 16 * (FALL - 1)
.check:
    add r13, 16

    lea rax, [map]
    add rax, [r13]
    mov rbx, [r13 + 8]
    imul rbx, SIZE

    mov byte [rax + rbx], '#'

    call clean
    call find

    cmp rax, -42
    jne .check
.end:
    mov rax, [r13]
    mov rbx, [r13 + 8]
    ret


clean:
    lea r8, [map]
    mov r9, r8
    add r9, SIZE * SIZE

    mov byte [r8], 0
.next:
    add r8, 1
    cmp r8, r9
    je .end

    cmp byte [r8], '#'
    je .next

    mov byte [r8], 0
    jmp .next
.end:
    ret