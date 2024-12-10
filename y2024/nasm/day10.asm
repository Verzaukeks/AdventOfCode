%define SIZE 60

section .data
    INPUT:  incbin "../inputs/d10"
            db 0
    FMT:    db "%d", 10, 0

section .bss
    SET:    resb SIZE * (SIZE + 2)

section .text
    global main
    extern printf
    default rel


main:
    sub rsp, 40

    call a1
    lea rcx, [FMT]
    mov rdx, r8
    call printf

    call a2
    lea rcx, [FMT]
    mov rdx, r9
    call printf

    add rsp, 40
    xor rax, rax
    ret


; r8: a1 result
; r9: a2 result
; r10: input pointer
; r11: counter
a1:
a2:
    xor r8, r8
    xor r9, r9
    lea r10, [INPUT]
    xor r11, r11
.next:
    push '0'
    push r10
    call count
    add rsp, 16
    add r11, 1
.skip:
    add r10, 1
    mov al, [r10]
    cmp al, 0
    jz .end
    cmp al, '0'
    je .next
    jmp .skip
.end:
    ret


; [rsp + 16]: expected char
; [rsp + 8]: input pointer
; rbx: input/set pointer
; rcx: temp ref pointer
count:
    mov rbx, [rsp + 8]
    lea rcx, [INPUT]
    cmp rbx, rcx
    jl .fail ; check lower_bound
    add rcx, SIZE * (SIZE + 2)
    cmp rbx, rcx
    jge .fail ; check upper_bound
    mov al, [rbx]
    cmp al, [rsp + 16]
    jne .fail ; check expected_char
    cmp al, '9'
    je .hit
    ; travel
    mov rax, [rsp + 16]
    add rax, 1
    push rax
    push rbx
    ; x+
    add qword [rsp], 1
    call count
    sub qword [rsp], 1
    ; x-
    sub qword [rsp], 1
    call count
    add qword [rsp], 1
    ; y+
    add qword [rsp], SIZE + 2
    call count
    sub qword [rsp], SIZE + 2
    ; y-
    sub qword [rsp], SIZE + 2
    call count
    add qword [rsp], SIZE + 2
    ; 
    add rsp, 16
    ret
.hit:
    add r9, 1
    mov rbx, [rsp + 8]
    lea rcx, [INPUT]
    sub rbx, rcx
    lea rcx, [SET]
    add rbx, rcx
    mov al, [rbx]
    cmp al, r11b
    je .fail
    mov [rbx], r11b
    add r8, 1
.fail:
    ret
