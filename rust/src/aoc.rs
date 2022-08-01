use std::fs;
use std::ops::{Add, Mul, Sub};

pub trait Solution {
    fn part01(&self, input: String) -> Option<String>;
    fn part02(&self, input: String) -> Option<String>;
}

pub fn read_input(year: i32, day: i8) -> String {
    let _day_placeholder = format!("{:0>2}", day);
    let _year = format!("{}", year);
    let file_path = &format!("./src/y{_year}/inputs/day{_day_placeholder}.txt");
    return fs::read_to_string(file_path).expect(&*format!("Could not read input file for day{_day_placeholder}, with path: {file_path}."));
}

#[derive(Debug, Copy, Clone, PartialEq, Eq, Hash)]
pub struct Point<T> {
    pub x: T,
    pub y: T,
}

impl Sub for Point<i64> {
    type Output = Self;

    fn sub(self, other: Self) -> Self::Output {
        Point {
            x: self.x - other.x,
            y: self.y - other.y,
        }
    }
}


impl Add for Point<i64> {
    type Output = Self;

    fn add(self, other: Self) -> Self::Output {
        Point {
            x: self.x + other.x,
            y: self.y + other.y,
        }
    }
}

impl Mul<i64> for Point<i64> {
    type Output = Self;

    fn mul(self, scalar: i64) -> Self::Output {
        Point {
            x: scalar*self.x,
            y: scalar*self.y,
        }
    }
}


