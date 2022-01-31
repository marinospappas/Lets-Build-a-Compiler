#
# My First TINSEL program
# demonstrates the basic capabilities of the language
# Accepts an integer n as input
# and prints the squares of all the integers from 1 to n
# 
# program tinselExample
.data
.align 8
	tinsel_msg_: .string "TINSEL version 1.0 for x86-84 (Linux) Dec 2021 (c) M.Pappas\n"
	newline_: .string "\n"
# variables declarations
	i:	.quad 0
	n:	.quad 0
