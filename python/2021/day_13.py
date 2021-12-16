from dataclasses import dataclass
from dataclasses import dataclass
from ..utils.file_utils import get_input
from ..utils.measurements import timed
from ..utils.math_utils import Point2D

@dataclass
class Instruction:
    axis: str
    value: int

def draw_dots_board(dots: set[Point2D]):
    board = ""
    for y in range(max(map(lambda p: p.y, dots))+1):
        for x in range(max(map(lambda p: p.x, dots))+1):
            board += "#" if Point2D(x,y) in dots else "."
        board += "\n"
    return board


@timed("Calculate result for part01/02:")
def fold_paper(dots: set[Point2D], instructions: list[Instruction]):
    def fold(old: int, cut: int) -> int:
        return 2*cut - old
    for idx, inst in enumerate(instructions):
        if inst.axis == 'x':
            dots = set([Point2D(p.x if p.x < inst.value else fold(p.x, inst.value), p.y) for p in dots])
        else:
            dots = set([Point2D(p.x, p.y if p.y < inst.value else fold(p.y, inst.value)) for p in dots])
        if(idx == 0):
            yield len(dots)
    
    yield draw_dots_board(dots)    


if __name__ == '__main__':
    dot_input, instructions_input = get_input().split("\n\n")
    initial_dots: set[Point2D] = set([Point2D(int(line.strip().split(",")[0]),int(line.strip().split(",")[1])) for line in dot_input.split("\n")])
    instructions: list[Instruction] = [Instruction(ax, int(value)) for ax, value in [line.replace("fold along ", "").split("=") for line in instructions_input.split("\n")]]
    for idx, res in enumerate(fold_paper(initial_dots, instructions)):
        print(f"Part0{idx+1}: \n{res}\n")
