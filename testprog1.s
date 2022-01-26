#
# My First TINSEL program
# demonstrates the basic capabilities of the language
# Accepts an integer n as input
# and prints the squares of all the integers from 1 to n
# 
# program tinselExample
# compiled on Wed Jan 26 17:25:50 CET 2022
.data
.align 8
	tinsel_msg_: .string "TINSEL version 1.1 for x86-84 (Linux) Jan 2022 (c) M.Pappas\n"
	newline_: .string "\n"
# variables declarations
	i:	.quad 0
	n:	.quad 0

.text
.align 8
.global _start
#
# calculate the square of i
# only for positive i
# 

# function i_square
i_square:
	pushq	%rbp
	movq	%rsp, %rbp
	movq	i(%rip), %rax
	testq	%rax, %rax
	pushq	%rax
	movq	$0, %rax
	testq	%rax, %rax
	popq	%rbx
	cmp	%rax, %rbx
	setle	%al
	andq	$1, %rax
	jz	i_square_L0_
	movq	$-1, %rax
	testq	%rax, %rax
	movq	%rbp, %rsp
	popq	%rbp
	ret
i_square_L0_:
	movq	i(%rip), %rax
	testq	%rax, %rax
	pushq	%rax
	movq	i(%rip), %rax
	testq	%rax, %rax
	popq	%rbx
	imulq	%rbx, %rax
	movq	%rbp, %rsp
	popq	%rbp
	ret

# main program
_start:
	pushq	%rbp
	movq	%rsp, %rbp
	pushq	%rbx
# print hello message
	lea	tinsel_msg_(%rip), %rdi
	call	write_s_

	lea	n(%rip), %rdi		# address of the variable to be read
	call	read_i_
	movq	%rax, n(%rip)
# main loop
main_L0_:
	movq	i(%rip), %rax
	testq	%rax, %rax
	pushq	%rax
	movq	$1, %rax
	testq	%rax, %rax
	popq	%rbx
	addq	%rbx, %rax
	movq	%rax, i(%rip)
	call	i_square
	movq	%rax, %rdi		# value to be printed in rdi
	call	write_i_
	movq	i(%rip), %rax
	testq	%rax, %rax
	pushq	%rax
	movq	n(%rip), %rax
	testq	%rax, %rax
	popq	%rbx
	cmp	%rax, %rbx
	setge	%al
	andq	$1, %rax
	jz	main_L0_
main_L1_:

# end of main
	movq	(%rbp), %rbx
	movq	%rbp, %rsp
	popq	%rbp
	movq	$60, %rax		# exit system call
	xorq	%rdi, %rdi		# exit code 0
	syscall

# end program

.data
	.align 8
# buffer for string operations - max str length limit
	string_buffer_:	.space 1024
