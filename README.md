CHMTIN016 - Tinotenda Chaminuka
I read in byte-by-byte from the address file, and used hexadecimal conversions to get a whole address, then converted it to decimal, found the corresponding page number and mapped it to a frame, then added the offset.
To run: make run ARGS="{filename}" e.g. make run ARGS="OS1sequence"