from copy import deepcopy
from dataclasses import dataclass
from enum import Enum, auto
from typing import Generic, Callable
from typing import TypeVar

T = TypeVar('T')


class Direction(Enum):
    FORWARD = auto(),
    DOWN = auto(),
    UP = auto(),


@dataclass
class Instruction:
    direction: Direction
    amount: int

    def __iter__(self):
        return iter((self.direction, self.amount))


lines = list(map(lambda x: Instruction(Direction[x[0].upper()], int(x[1])),
                 [distance.strip().split(' ') for distance in open("inputs/day_02.txt", "r").readlines()]))


class Navigator(Generic[T]):
    def __init__(
            self,
            initial_ship_data: T,
            operations: dict[Direction, Callable[[T, int], T]],
            calc_position: Callable[[T], int]
    ) -> None:
        super().__init__()
        self.initial_ship_data = initial_ship_data
        self.operations = operations
        self.calc_position = calc_position

    def navigate(self, instructions: list[Instruction]) -> int:
        cur_ship_data = deepcopy(self.initial_ship_data)
        for direction, amount in instructions:
            cur_ship_data = self.operations[direction](cur_ship_data, amount)
        return self.calc_position(cur_ship_data)


@dataclass
class BasicData:
    position: int
    depth: int


class NavigatorPart01(Navigator[BasicData]):
    def __init__(self) -> None:
        super().__init__(
            BasicData(position=0, depth=0),
            {
                Direction.FORWARD: lambda data, amount: BasicData(position=data.position + amount, depth=data.depth),
                Direction.DOWN: lambda data, amount: BasicData(position=data.position, depth=data.depth + amount),
                Direction.UP: lambda data, amount: BasicData(position=data.position, depth=data.depth - amount),
            },
            lambda d: d.position * d.depth
        )


@dataclass
class AngleData(BasicData):
    aim: int


class NavigatorPart02(Navigator[BasicData]):
    def __init__(self) -> None:
        super().__init__(
            AngleData(position=0, depth=0, aim=0),
            {
                Direction.FORWARD: lambda data, amount: AngleData(position=data.position + amount, depth=data.depth + data.aim * amount, aim=data.aim),
                Direction.DOWN: lambda data, amount: AngleData(position=data.position, depth=data.depth, aim=data.aim + amount),
                Direction.UP: lambda data, amount: AngleData(position=data.position, depth=data.depth, aim=data.aim - amount),
            },
            lambda d: d.position * d.depth
        )


def part01(instructions: list[Instruction]):
    position = 0
    depth = 0
    for direction, amount in instructions:
        match direction:
            case Direction.FORWARD:
                position = position + amount
            case Direction.DOWN:
                depth = depth + amount
            case Direction.UP:
                depth = depth - amount
    return position * depth


def part02(instructions: list[Instruction]):
    position = 0
    depth = 0
    aim = 0

    for direction, amount in instructions:
        match direction:
            case Direction.FORWARD:
                position = position + amount
                depth = depth + aim * amount
            case Direction.DOWN:
                aim = aim + amount
            case Direction.UP:
                aim = aim - amount

    return position * depth


if __name__ == '__main__':
    print(f"Part01_basic:\t {part01(lines)}")
    print(f"Part01_generic:\t {NavigatorPart01().navigate(lines)}")
    print(f"Part02_basic:\t {part02(lines)}")
    print(f"Part02_generic:\t {NavigatorPart02().navigate(lines)}")
