#include <iostream>
#include <vector>

int main()
{
    // Print numbers from 1 to 100
    for (int i = 1; i <= 10000000; ++i)
    {
        std::cout << i << std::endl;
    }

    // Attempt to allocate a large amount of memory
    std::vector<int> large_memory;
    large_memory.resize(1e8); // Attempt to allocate space for 100 million ints
    std::fill(large_memory.begin(), large_memory.end(), 1);

    return 0;
}
