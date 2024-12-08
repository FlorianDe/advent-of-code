#include <utility>

template<typename T>
struct ComplexHash {
    std::size_t operator()(const std::complex<T>& c) const {
        return std::hash<T>()(c.real()) ^ std::hash<T>()(c.imag());
    }
};

template<typename T>
struct ComplexEqual {
    bool operator()(const std::complex<T>& c1, const std::complex<T>& c2) const {
        return c1.real() == c2.real() && c1.imag() == c2.imag();
    }
};