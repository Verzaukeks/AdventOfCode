%define MAXN (36*36*36)

section .data
    INPUT:  incbin "../inputs/d24"
            db 0, 0, 0, 0, 0
    FMT:    db "%lld", 10, 0

section .bss
    sigs:   resb MAXN
    gates:  resq 4 * 1000
    sol:    resb 1_000_000

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
    lea rcx, [sol]
    xor rax, rax
    call printf

    add rsp, 32
    xor rax, rax
    ret


%macro cmprs 1
    cmp %1, 'a'
    jl %%sub0
    add %1, 10 + '0' - 'a'
%%sub0:
    sub %1, '0'
%endmacro

%macro compress 3
    cmprs %1
    cmprs %2
    cmprs %3
    imul %1, 36
    add %1, %2
    imul %1, 36
    add %1, %3
%endmacro

%macro dcmprs 0
    mov rcx, 36
    xor rdx, rdx
    idiv rcx
    cmp rdx, 10
    jl %%add0
    add rdx, 'a' - '0' - 10
%%add0:
    add rdx, '0'
%endmacro

%macro decompress 1
    mov %1, ' '
    dcmprs
    shl %1, 8
    or %1, rdx
    dcmprs
    shl %1, 8
    or %1, rdx
    dcmprs
    shl %1, 8
    or %1, rdx
%endmacro


parse:
    lea r8, [INPUT]
    lea r9, [sigs]
.next_sigs:
    xor rax, rax
    xor rbx, rbx
    xor rcx, rcx
    xor rdx, rdx

    mov al, [r8]
    mov bl, [r8 + 1]
    mov cl, [r8 + 2]
    mov dl, [r8 + 5]
    cmp al, 13
    je .gates

    compress rax, rbx, rcx
    sub dl, '0'
    mov byte [r9 + rax], dl

    add r8, 8
    jmp .next_sigs
.gates:
    add r8, 2
    lea r9, [gates]
.next_gates:
    xor rax, rax
    xor rbx, rbx
    xor rcx, rcx

    mov al, [r8]
    mov bl, [r8 + 1]
    mov cl, [r8 + 2]
    add r8, 4

    cmp cl, 0
    je .end

    compress rax, rbx, rcx
    mov [r9], rax

    mov al, [r8]
    mov [r9 + 8], al

    cmp al, 'O'
    je .j3
    add r8, 1
.j3:
    add r8, 3

    xor rax, rax
    mov al, [r8]
    mov bl, [r8 + 1]
    mov cl, [r8 + 2]
    add r8, 7

    compress rax, rbx, rcx
    mov [r9 + 16], rax

    xor rax, rax
    mov al, [r8]
    mov bl, [r8 + 1]
    mov cl, [r8 + 2]
    add r8, 5

    compress rax, rbx, rcx
    mov [r9 + 24], rax

    add r9, 32
    jmp .next_gates
.end:
    mov qword [r9], -1
    ret


a1:
    xor rsi, rsi
    lea r8, [sigs]
.next:
    lea rdi, [gates]
.step:
    mov rax, [rdi]
    mov rbx, [rdi + 8]
    mov rcx, [rdi + 16]
    mov rdx, [rdi + 24]
    add rdi, 32

    cmp rax, -1
    je .step_end
    
    mov al, [r8 + rax]
    mov cl, [r8 + rcx]
    and rax, 1
    and rcx, 1

    cmp rbx, 'A'
    je .and
    cmp rbx, 'X'
    je .xor
    cmp rbx, 'O'
    je .or
.and:
    and rax, rcx
    jmp .write
.xor:
    xor rax, rcx
    jmp .write
.or:
    or rax, rcx
.write:
    mov [r8 + rdx], al
    jmp .step
.step_end:
    add rsi, 1
    cmp rsi, 300
    jne .next

    xor rax, rax
%assign i 63
%rep 64
    mov rbx, 'z'
    mov rcx, '0' + (i / 10)
    mov rdx, '0' + (i % 10)
    compress rbx, rcx, rdx
    mov bl, [r8 + rbx]
    shl rax, 1
    or al, bl
%assign i i-1
%endrep
    ret


; analyzed it with
; "make day24 & day24 | tail -n +2 | d2 --layout elk - > day24.svg"
a2:
    lea rsi, [gates]
    lea rdi, [sol]

%assign i 0
%rep 46
    mov byte  [rdi +  0], 'x'
    mov byte  [rdi +  1], '0' + (i / 10)
    mov byte  [rdi +  2], '0' + (i % 10)
    mov dword [rdi +  3], ".sha"
    mov dword [rdi +  7], "pe: "
    mov dword [rdi + 11], "hexa"
    mov dword [rdi + 15], "gon "
    mov byte  [rdi + 19], 10
    add rdi, 20

    mov byte  [rdi +  0], 'y'
    mov byte  [rdi +  1], '0' + (i / 10)
    mov byte  [rdi +  2], '0' + (i % 10)
    mov dword [rdi +  3], ".sha"
    mov dword [rdi +  7], "pe: "
    mov dword [rdi + 11], "diam"
    mov dword [rdi + 15], "ond "
    mov byte  [rdi + 19], 10
    add rdi, 20

    mov byte  [rdi +  0], 'z'
    mov byte  [rdi +  1], '0' + (i / 10)
    mov byte  [rdi +  2], '0' + (i % 10)
    mov dword [rdi +  3], ".sha"
    mov dword [rdi +  7], "pe: "
    mov dword [rdi + 11], "cyli"
    mov dword [rdi + 15], "nder"
    mov byte  [rdi + 19], 10
    add rdi, 20
%assign i i+1
%endrep

.next:
    mov rax, [rsi + 24]
    decompress r8
    mov rbx, [rsi + 8]

    mov [rdi], r8
    mov word [rdi + 3], ': '
    mov [rdi + 5], bl
    mov word [rdi + 6], '\n'
    mov [rdi + 8], r8
    mov byte [rdi +  11], 10
    add rdi, 12

    mov rax, [rsi]
    decompress r9
    mov [rdi], r9
    mov dword [rdi + 3], " -> "
    mov [rdi + 7], r8
    mov byte [rdi + 10], 10
    add rdi, 11

    mov rax, [rsi + 16]
    decompress r9
    mov [rdi], r9
    mov dword [rdi + 3], " -> "
    mov [rdi + 7], r8
    mov byte [rdi + 10], 10
    add rdi, 11

    add rsi, 32
    cmp qword [rsi], -1
    jne .next
    ret
