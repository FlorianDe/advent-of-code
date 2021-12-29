from __future__ import annotations

from dataclasses import dataclass
from typing import Optional, TypeVar

T = TypeVar('T')


class Func:
    @staticmethod
    def identity(x: T) -> T:
        return x


@dataclass(frozen=True, eq=True)
class Point2D:
    x: int = 0
    y: int = 0

    @staticmethod
    def from_tuple(values: tuple[str | int, ...]) -> Point2D:
        return Point2D(x=int(values[0]), y=int(values[1]))

    def copy(self) -> Point2D:
        return Point2D(self.x, self.y)

    # def normalize(self) -> Point2D:
    #     l2 = math.sqrt(self.x*self.x + self.y+self.y)
    #     return Point2D(self.x/l2, self.y/l2)

    def __add__(self, other):
        return Point2D(self.x + other.x, self.y + other.y)

    def __sub__(self, other):
        return Point2D(self.x - other.x, self.y - other.y)

    def step(self, direction: Point2D, steps: Optional[int] = None):
        cur = self.copy()

        while steps != 0:
            yield cur
            if steps is not None:
                steps -= 1
            cur = cur + direction

    def __lt__(self, other):
        return self.x < other.x or self.y < other.y



@dataclass(frozen=True, eq=True)
class Line2D:
    origin: Point2D
    target: Point2D

    def det(self):
        return self.origin.x * self.target.y - self.origin.y * self.target.x

    def line_intersection(self, line: Line2D):
        xdiff = Point2D(self.origin.x - self.target.x, line.origin.x - line.target.x)
        ydiff = Point2D(self.origin.y - self.target.y, line.origin.y - line.target.y)

        div = Line2D(xdiff, ydiff).det()
        if div == 0:
            raise Exception('lines do not intersect')

        d = Point2D(self.det(), line.det())
        x = Line2D(d, xdiff).det() / div
        y = Line2D(d, ydiff).det() / div
        return x, y

    @staticmethod
    def from_tuple(values: tuple[Point2D, ...]) -> Line2D:
        return Line2D(values[0], values[1])
