#include <iostream>
int main()
{
  const size_t numIntegers = 10 * 1024 * 1024 / sizeof(int);
  int *array = new int[numIntegers];
  for (size_t i = 0; i < numIntegers; ++i)
  {
    array[i] = 0;
  }
  std::cout << "Array of" << "numIntegers" << "integers initialized" << std::endl;
  return 0;
}