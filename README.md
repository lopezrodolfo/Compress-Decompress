# File Compression and Decompression Program

This Java program provides functionality to compress ASCII text files into smaller binary files and decompress them back to their original form.

## Authors

- Rodolfo Lopez
- Russell Gokemeijer

## Date

5/5/2021

## Features

- Compress ASCII text files into binary files with `.zzz` extension
- Decompress `.zzz` files back to original ASCII text
- Generate log files for both compression and decompression processes

## Usage

### Compression

```
java Compress <input_file>
```

- Compresses `<input_file>` into `<input_file>.zzz`
- Generates `<input_file>.log` with compression details

### Decompression

```
java Decompress <input_file>.zzz
```

- Decompresses `<input_file>.zzz` back to `<input_file>`
- Generates `<input_file>.zzz.log` with decompression details

## Files

- `Compress.java`: Main compression program
- `Decompress.java`: Main decompression program
- `Compressor.java`: Implements compression algorithm
- `Decompressor.java`: Implements decompression algorithm
- `SeperateChain.java`: Hash table implementation
- `OurPat.java`: Pattern object for hash table entries

## Implementation Details

- Uses a perfect hash table for efficient pattern lookup
- Implements separate chaining for collision resolution
- Dynamically resizes the hash table as needed

## Log File Information

Compression log includes:

- Original and compressed file sizes
- Compression time
- Number of dictionary entries
- Number of hash table rehashes

Decompression log includes:

- Decompression time
- Number of table doublings
