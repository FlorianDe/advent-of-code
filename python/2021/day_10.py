from dataclasses import dataclass
from typing import Optional

from utils.file_utils import get_input_lines
from utils.measurements import timed

syntax_score_mappings: dict[str, int] = {
    ")": 3,
    "]": 57,
    "}": 1197,
    ">": 25137,
}
linter_score_mappings: dict[str, int] = {
    "(": 1,
    "[": 2,
    "{": 3,
    "<": 4,
}
opposites: dict[str, str] = {
    ")": "(",
    "]": "[",
    "}": "{",
    ">": "<",
}
closing_brackets_set = set([k for k in opposites.keys()])
opening_brackets_set = set([v for v in opposites.values()])

symbol_lines = [symbol.strip() for symbol in get_input_lines()]


@dataclass
class BracketSyntaxError:
    expected: str
    actual: str


@dataclass
class BracketReplacementResult:
    result: Optional[str] = None
    syntax_error: Optional[BracketSyntaxError] = None


def replace_brackets(line: str) -> BracketReplacementResult:
    while True:
        right = 0
        closing_found = ""
        for symbol in line:
            if symbol in closing_brackets_set:
                closing_found = symbol
                break
            right += 1
        if right == len(line):
            return BracketReplacementResult()

        same_closings_found = 0
        for left in range(right - 1, -1, -1):
            if line[left] == closing_found:
                closing_found += 1
            elif line[left] == opposites[closing_found]:
                if same_closings_found == 0:
                    return BracketReplacementResult(result=line[0: left:] + line[right + 1::])
                else:
                    same_closings_found -= 1
            elif line[left] in opening_brackets_set:
                return BracketReplacementResult(
                    syntax_error=BracketSyntaxError(expected=line[left], actual=line[right])
                )


@timed("Calculate results:")
def calculate_scores(lines: list[str]) -> tuple[int, int]:
    syntax_error_score = 0
    linter_scores = []
    for line in lines:
        line_result: Optional[str] = line
        line_has_errors = False
        while line_result is not None:
            replace_res = replace_brackets(line_result)
            line_result = replace_res.result
            error = replace_res.syntax_error
            if error is not None:
                line_has_errors = True
                syntax_error_score += syntax_score_mappings[error.actual]
                break
        if not line_has_errors:
            stack = []
            for symbol in line:
                if symbol in opening_brackets_set:
                    stack.append(symbol)
                elif symbol in closing_brackets_set:
                    if stack[-1] == opposites[symbol]:
                        stack.pop()
                    else:
                        raise ValueError("Should not happen")
            linter_score = 0
            for symbol in reversed(stack):
                linter_score = 5 * linter_score + linter_score_mappings[symbol]
            linter_scores.append(linter_score)
    linter_scores = sorted(linter_scores)
    return syntax_error_score, linter_scores[len(linter_scores)//2]


if __name__ == '__main__':
    syntax_error_score, linter_score = calculate_scores(symbol_lines)
    print(f"Part01: {syntax_error_score}")
    print(f"Part02: {linter_score}")
