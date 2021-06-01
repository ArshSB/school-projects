//-----------------------------------------
// NAME: Arshpreet Buttar 
// COURSE: COMP 2160
// 
// REMARKS: 
// This program implements garbage collection as instructed for assignment 4.
// It follows the interface and implementation details as given in ObjectManager.h file
//----------------------------------------

#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include "ObjectManager.h"

typedef struct NODE {

	Ref ref;   // reference for this block 
	ulong start; // start address of this particular block
	ulong size;  // size of block in buffer in bytes
	ulong count; // amount of references to this block

	struct NODE *next;  
	// pointer to linked list next node which represents the succeeding memory block 

} Node;

static void validateManager();
static void compact();
static Node * searchReference(Ref);

// holds the current reference being used and thus the next reference to use 
static Ref currentRef;

// holds the first node of the linked list
static Node *head;
// holds the last node of linked list
static Node *tail;
// holds number of nodes in linked list (in other words the number of memory blocks in buffer)
static int numNodes;

// buffers for defragment, one buffer will be in use while other will be used to perform defragmentation
// when the buffer currently in use becomes full
static uchar buffer_1[MEMORY_SIZE];
static uchar buffer_2[MEMORY_SIZE];
// pointer to indicate which 1 of the 2 buffers is currently in use
static uchar *currentBuffer;
// holds the address location to the next free memory in buffer
static ulong freeMem;


static void validateManager() {

	assert(currentRef > 0);
	assert(numNodes >= 0);
	assert(freeMem < MEMORY_SIZE);
	assert(currentBuffer == buffer_1 || currentBuffer == buffer_2);

	// check if head and tail is null if no nodes
	if (numNodes == 0)
    	assert(head == NULL && tail == NULL);

	// if 1 node is present, then head and tail should be same and next node from head should always be null 
  	else if (numNodes == 1)
    	assert(head == tail && head->next == NULL && tail->next == NULL);
  	
	// otherwise for 1+ nodes, both head and tail should be null
	else
    	assert( head != NULL && tail != NULL);

}

// Purpose: This function initializes the manager and sets important variables to their
// respective intial values. 
// Input/Output: Does not take anything as argument or return any 
// variable or print to STDOUT
void initPool() {

	// destroy pool bfore intializing if list is not empty
	if (head != NULL && numNodes > 0) {
		assert(currentBuffer != NULL);
		destroyPool();
	}

	// start from 1 as 0 is reserved for NULL_REF
	currentRef = 1;
	head = NULL;
	tail = NULL;
	numNodes = 0;
	currentBuffer = buffer_1;
	freeMem = 0;

	validateManager();
}

// Purpose: This function removes all nodes from linked list and thus destroys the manager
// Input/Output: Does not take anything as argument or return any 
// variable. However, this function prints to STDOUT to indicate if pool was destroyed
// or if it was already empty
void destroyPool() {

	validateManager();

	if (head != NULL && numNodes > 0) {
		// current node that is initially set to head node
		Node * current = head;
		// next node from current node initailly set to NULL
		Node * nextNode = NULL;

		// iterate the entire list 
		for (int node = 0; node < numNodes; node++) {
			
			assert(current != NULL);
			// set next node to the succedding node from current node
			nextNode = current->next;
			// deallocate memory of current node
			free(current);
			// now simply set current node as the next node to be deleted on next iteration
			current = nextNode;

		}
		// after deleting every node in linked list, set head NULL to indicate the list is now empty
		head = NULL;
		tail = NULL;
		numNodes = 0;

		printf("\nPool destroyed\n\n");
	}

	else
		printf("\nPool is already empty\n\n");

	// either pool was destroyed or it was already empty, in each both head and tail should be NULL
	assert(head == NULL && tail == NULL && numNodes == 0);
}

// Purpose: This function prints the relevant information of the 
// memory blocks which occupy the object manager
// Input/Output: Does not take anything as argument or return any 
// variable. Only prints to STDOUT
void dumpPool() {
	
	validateManager();

	// only dump if pool is not empty
	if (head != NULL && tail != NULL && numNodes > 0) {

		// current node for iteration
		Node * current = head;
		
		// iterate through the entire linked list
		for (int node = 0; node < numNodes; node++) {

			assert(current != NULL);

			printf("Reference ID: %lu || Starting location: %lu || Reference count: %lu || Size: %lu bytes\n", current->ref, current->start, current->count, current->size);

			current = current->next;

		}
	}

	else {

		assert(head == NULL && tail == NULL && numNodes == 0);
		printf("\nPool is empty\n\n");

	}
}

// Purpose: This function inserts a new memory block of given size to the manager.
// It also performs garbage collection if necessary based on the current state of
// the manager.
// Input/Output: Takes unsigned long input that represents the size of the memory block
// to be inserted. Returns 0 (NULL_REF) if insertion was not performed 
// (due to invalid size or not enough space in buffer) or if insertion was successful
// it returns the corresponding reference ID for the memory block
Ref insertObject(const ulong size) {

	// result set to NULL_REF initially, indicating object was not inserted
	Ref resultRef = NULL_REF;

	assert(size >= 0);
	// if the size of object to be inserted exceeds memory size, then execute garbage collection
	if (size > (MEMORY_SIZE - freeMem) )
		compact();

	// perform insertion
	if(size < (MEMORY_SIZE - freeMem) && size > 0) {

		assert(size < (MEMORY_SIZE - freeMem) );

		// new node that holds information of object to be inserted
		Node * newNode = (Node *) malloc(sizeof(Node));

		assert(newNode != NULL);
		// only continue if malloc was sucessful
		if (newNode != NULL) {

			assert(currentRef >= 1);
			assert(freeMem < MEMORY_SIZE);
		
			// set the object information
			newNode->ref = currentRef;
			newNode->start = freeMem;
			newNode->size = size;
			newNode->count = 1;

			// since new nodes are inserted at tail, the succeeding node will be NULL
			newNode->next = NULL;
			
			// if there is 1+ nodes in list, simple perform tail insertion
			if (numNodes >= 1) {

				assert(head != NULL);
				tail->next = newNode;
				tail = tail->next;

			}
			
			// otherwise list is empty in which case set both head and tail pointers to the new node
			else {	

				assert(head == NULL && numNodes == 0);
				head = newNode;
				tail = head;

			}
			
			// update number of nodes in list, the next index of free memory and set flag to the respective reference
			numNodes++;
			freeMem += size;
			resultRef = currentRef++;

			assert(currentRef >= 1);
			assert(freeMem < MEMORY_SIZE);
		}
	}

	if (resultRef == NULL_REF)
		printf( "Unable to successfully complete memory allocation request.\n" );


	validateManager();
	// either object was not inserted or it is in linked list now
	assert(resultRef == NULL_REF || searchReference(resultRef) != NULL);
	return resultRef;
}

// Purpose: This function retrieves information of a memory block in manager using
// the given reference ID. 
// Input/Output: Takes unsigned long representing the reference ID of the memory 
// block to be retrieved if it exists. Returns void pointer to the address of the memory block
// in buffer if a memory block of given reference ID exists. If, not it exits execution
// to prevent program failure
void * retrieveObject(const Ref ref) {

	// set void pointer to be returned to NULL initially if no node with ref exists
	void * objectPtr = NULL;

	assert(ref >= 0);
	// since ref is unsigned long, it will always be >= 0 so need to check that
	// check if ref to be found is less than currentRef
	if (ref < currentRef) {

		// holds node copy of the node to be searched in list with the given ref
		Node * searchNode = searchReference(ref);

		// check if the search does not return false (meaning node to be searched is not found)
		if (searchNode != NULL) {

			assert(head != NULL && numNodes > 0);
			// set pointer to starting address of the search node
			objectPtr = &currentBuffer[searchNode->start];

		}
	}

	validateManager();

	// if object is not found and objectPtr is NULL, exit execution to avoid complete program failure 
	if (objectPtr == NULL) {
		printf("Invalid reference exception with reference %lu, terminating process.\n", ref);
		exit(EXIT_FAILURE);
	}

	// either object is not null (sucessfully retrieved) or it is not in linked list
	assert(objectPtr != NULL);
	return objectPtr;
}

// Purpose: This function searches for a memory block with given reference ID in manager
// Input/Output: Takes unsigned long input representing the reference ID of memory block
// to be searched. Returns Node pointer copy of the search node if it exists, returns
// NULL otherwise
static Node * searchReference(const Ref ref) {

	validateManager();

	// search node to be returned which will hold the copy of node with ref if it exists
	Node * searchNode = NULL;

	assert(ref >= 0);
	// since ref is unsigned long, it will always be >= 0 so need to check that
	if (head != NULL && ref <= currentRef) {

		assert(numNodes > 0);
		// current node for iteration
		Node * current = head;

		// keep iterating until the end of list or search node is found
		for (int node = 0; node < numNodes && searchNode == NULL; node++) {

			assert(current != NULL);

			if (current->ref == ref)
				searchNode = current;
			
			current = current->next;
		}
	}

	assert(searchNode == NULL || searchNode->count >= 1);
	return searchNode;
}

// Purpose: This function performs garbage collection using the techniques described in assignment
// description. Prints garbage collection information after performing to let the user know about 
// garbage collector details
// Input/Output: Does not take anything as argument or return any 
// variable. 
static void compact() {

	validateManager();
	// variable to calculate total number of bytes collected at end
	int prevFreeMem = freeMem;
	// variable to store number of objects in buffer
	int numObjects = 0;
	// new buffer pointer that will be used to perform defragmentation
	uchar *newBuffer;
	// set freeMem to 0 to indicate new buffer in use
	freeMem = 0;

	// check which one of the 2 buffers is in use so the new buffer can be set to the unused one
	if (currentBuffer == buffer_1)
		newBuffer = buffer_2;
	
	else	
		newBuffer = buffer_1;

	// current node for iteration
	Node * current = head;

	// iterate through entire list
	for (int node = 0; node < numNodes; node++) {

		assert(current != NULL);
		assert(current->size >= 0 && current->count > 0 && current->ref > 0);
		assert(current->start >= 0 && current->start < MEMORY_SIZE);

		// store the starting address of current node to perform deep copy
		int startAddress = current->start;
		// update new starting address of object in new buffer
		current->start = freeMem;

		// perform deep copy of object from current buffer to new buffer
		for (int byte = startAddress; byte < current->size + startAddress; byte++) {
			
			// copy byte at current buffer to new buffer at the respective index
			newBuffer[freeMem++] = currentBuffer[byte];

		}
		numObjects++;
		current = current->next;
	}

	// after completing defragmentation, set currentBuffer to new buffer so that other functions can refer to it
	currentBuffer = newBuffer;

	assert(freeMem < MEMORY_SIZE && currentBuffer != NULL);
	validateManager();

	printf("\nGarbage collector information:\n");
	printf("Number of objects that exist: %d || Current number of bytes in use: %lu || Number of bytes collected: %lu\n\n", numObjects, freeMem, (prevFreeMem - freeMem));

}

// Purpose: This function adds a new reference to a memory block in manager with given reference ID, if it exists
// Input/Output: Takes unsigned long input representing the reference ID of memory block. Does not return
// anything
void addReference(const Ref ref) {

	// get the node with given ref
	Node * searchNode = searchReference(ref);

	assert(ref >= 0);
	// if the search node exists, increase the count to add another reference
	if (searchNode!= NULL && ref <= currentRef) {

		assert(head != NULL && numNodes > 0);
		searchNode->count++;
		assert(searchNode->count >= 1);
	}

	validateManager();
}

// Purpose: This function removes a reference from a memory block in manager with given reference ID, if it exists
// If the number of references of given memory block reaches 0, the memory block is 'freed' from the buffer
// Input/Output: Takes unsigned long input representing the reference ID of memory block. Does not return
// anything
void dropReference(const Ref ref) {

	// get the node with given ref, if it exists
	Node * searchNode = searchReference(ref);
	
	assert(ref >= 0);
	// check if the node exists in order to drop reference
	if (searchNode != NULL && ref <= currentRef) {

		assert(numNodes > 0);

		// if the search node has 1 or fewer references, then it must be deleted
		if (searchNode->count <= 1) {

			// perform deletion at head if the search node is at start of list
			if (searchNode == head) {
				
				assert(head != NULL);
				head = head->next;
				free(searchNode);

			}

			// otherwise perform deletion at middle or tail
			else if (head != NULL) {
				
				// previous and current node pointers to perform iteration and deletion
				Node * previous = head;
				Node * current = previous->next;

				// iterate through the linked list until the end or until the search node
				for (int node = 0; node < numNodes && current != searchNode; node++) {

					assert(previous != NULL);
					previous = current;
					current = current->next;

				}

				// if the search node is at end of list, perform deletion at tail
				if (current == tail) {
					
					assert(tail != NULL);
					tail = previous;
					previous->next = NULL;
					free(current);
				
				}
				
				// otherwise the search node is somewhere in the middle
				else {
					
					// set succeeding node from previous to succeeding node from current (search node)
					previous->next = current->next;
					// now current is deferenced from list so we can safely free it
					free(current);
				
				}
			}
			// finally update the number of nodes in list
			numNodes--;

			// if only one node exists, then head and tail are same
			if (numNodes <= 1)
				tail = head;
		}

		// if the number of references of search node is greater than 1, simply drop reference
		else
			searchNode->count--;

	}

	validateManager();
}