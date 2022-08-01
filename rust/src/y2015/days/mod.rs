use crate::aoc::Solution;

pub mod day01;
pub mod day02;

pub fn get_solution(day: i8) -> &'static dyn Solution {
    match day {
        1 => &day01::Day01 {},
        2 => &day02::Day02 {},
        _ => panic!("No Solution found for year 2015 day {:0>2}", day)
    }
}
