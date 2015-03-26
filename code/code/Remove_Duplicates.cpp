// code.cpp : 定义控制台应用程序的入口点。
//

#include "stdafx.h"
#include <iostream>
using namespace

class Solution
{
public:
	int removeDuplicates(int a[], int n)
	{
		if (n <= 2) return n;
		int index = 2;
		for (int i = 2; i < n; i++)
		{
			if (A != A[index - 2])
			{
				A[index++] = A[i];
			}

		}
		return index;
		cout << index;
	}
}
int _tmain(int argc, _TCHAR* argv[])
{
	Solution s1, s2;
	int A[] = { 1, 1, 2, 3, 3, 1, 4, 5, 6, 7, 1, 5, 6, 7, 4 };
	int n = A.length;
	int n1;
	cout << n;
	n1 = s1.removeDuplicates(A, n);
	cout << n1;
		
	return 0;
}

