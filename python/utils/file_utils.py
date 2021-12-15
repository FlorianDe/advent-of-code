import sys
from pathlib import Path
from typing import TextIO

def get_input_file() -> TextIO:
    path = Path(sys.argv[0])
    filename = str(path.name)
    return open(f"{path.parent}/inputs/{filename.replace('.py', '.txt')}", "r")

def get_input() -> str:
    return get_input_file().read()


def get_input_lines() -> list[str]:
    return get_input_file().readlines()
