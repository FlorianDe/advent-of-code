# Compiler settings
CXX = /opt/homebrew/opt/llvm/bin/clang++
CXXFLAGS = -std=c++23 -stdlib=libc++ -Wall -Wextra -O3

# Directories
SRC_DIR = src
OUT_DIR = build

# Dynamic variables
YEAR ?= 
DAY ?= 
FILES =

# Ensure YEAR is provided
ifeq ($(YEAR),)
    $(error You must specify a YEAR. Example: make run YEAR=2023)
endif

# Handle the DAY argument and validate if it's passed
ifeq ($(DAY),)
    FILES = $(wildcard $(SRC_DIR)/$(YEAR)/day*.cpp)
else
    FILES = $(SRC_DIR)/$(YEAR)/day$(DAY).cpp
    ifeq ($(wildcard $(FILES)),)
        $(error File src/$(YEAR)/day$(DAY).cpp does not exist)
    endif
endif

# Output executables
OUTS = $(patsubst $(SRC_DIR)/$(YEAR)/%.cpp, $(OUT_DIR)/$(YEAR)/%, $(FILES))

# Create output directories if they don't exist
$(shell mkdir -p $(OUT_DIR)/$(YEAR))

# Default target: Compile the executables
all: $(OUTS)

# Compile rule: Compile each .cpp file into its corresponding executable
$(OUT_DIR)/$(YEAR)/%: $(SRC_DIR)/$(YEAR)/%.cpp
	@echo "Compiling $<"
	$(CXX) $(CXXFLAGS) $< -o $@

# Run target: Run all executables
run: all
	@echo "Running all compiled files..."
	@for exe in $(OUTS); do \
		echo "Running $$exe"; \
		$$exe; \
	done

# Clean target: Remove all compiled output
clean:
	rm -rf $(OUT_DIR)

.PHONY: all run clean