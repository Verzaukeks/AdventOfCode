CC   = gcc
NASM = nasm

CC_FLAGS   = -m64 -g
NASM_FLAGS = -f elf64 -g

.PHONY: clean
.SILENT:

%: %.o
	$(CC) $(CC_FLAGS) $< -o $@

%.o: %.asm
	$(NASM) $(NASM_FLAGS) $< -o $@

clean:
	rm -f *.o *.exe

# win x86-64 C Call Order
# RCX, RDX, R8, R9, STACK

# RAX: Accumulator Register
# RBX: Base Register
# RCX: Count Register
# RDX: Data Register
# RSI: Source Index
# RDI: Destination Index
# R8-R15: Extended General-Purpose Registers

# RSP: Stack Pointer
# RBP: Base Pointer
# RIP: Instruction Pointer
