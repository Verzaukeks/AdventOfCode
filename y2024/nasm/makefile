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

# Microsoft x64 calling convention
# https://en.wikipedia.org/wiki/X86_calling_conventions#Microsoft_x64_calling_convention

# Parameters:      RCX, RDX, R8, R9, STACK
# Shadow-Space:    32 Bytes
# Stack-Alignment: 16 Bytes

# RAX: Accumulator Register (volatile)
# RBX:        Base Register
# RCX:       Count Register (volatile)
# RDX:        Data Register (volatile)
# RSI:      Source Index
# RDI: Destination Index
# R8 -R11: Extended General-Purpose Registers (volatile)
# R12-R15: Extended General-Purpose Registers 

# RSP:       Stack Pointer
# RBP:        Base Pointer
# RIP: Instruction Pointer
