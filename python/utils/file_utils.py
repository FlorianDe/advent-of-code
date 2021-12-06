import os

import sys
from typing import TextIO


def get_input_file() -> TextIO:
    path = sys.argv[0]
    filename = os.path.basename(path)
    return open(f"inputs/{filename.replace('.py', '.txt')}", "r")


def get_input() -> str:
    return get_input_file().read()


def get_input_lines() -> list[str]:
    return get_input_file().readlines()
