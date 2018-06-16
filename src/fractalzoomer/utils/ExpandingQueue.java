/*
 * Copyright (C) 2018 hrkalona
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fractalzoomer.utils;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 *
 * @author hrkalona
 */
public class ExpandingQueue<Item> {

    private ArrayList<Item[]> LinkedQueues;
    private Item[] headQueue;
    private Item[] tailQueue;
    private int size;
    //private int max_size = 0;
    private int head;
    private int tail;
    private int initialAllocation;

    public ExpandingQueue(int initSize) {

        LinkedQueues = new ArrayList<Item[]>(1000);
        head = tail = 0;
        headQueue = tailQueue = (Item[]) new Object[initSize];
        LinkedQueues.add(headQueue);
        size = 0;
        initialAllocation = initSize;

    }

    /**
     * Returns the size of the queue
     *
     * @return Integer representing the number of items in the queue
     */
    public int size() {
        return this.size;
    }

    /**
     * Returns whether or not the queue is empty
     *
     * @return True if the queue is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException("Item must not be null");
        }

        int locationInQueue = tail % initialAllocation;

        tailQueue[locationInQueue] = item;
        tail++;
        size++;

        if (locationInQueue == initialAllocation - 1) {
            tailQueue = (Item[]) new Object[initialAllocation];
            LinkedQueues.add(tailQueue);
        }

        /*if (size > max_size) {
            max_size = size;
        }*/
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("There is nothing in the queue");
        }

        int locationInQueue = head % initialAllocation;

        Item item = headQueue[locationInQueue];
        headQueue[locationInQueue] = null;

        if (locationInQueue == initialAllocation - 1) {
            int currentQueue = head / initialAllocation;
            LinkedQueues.set(currentQueue, null);
            headQueue = LinkedQueues.get(currentQueue + 1); //assuming that tail is always greater or equal to head, this access shoud be fine
        }

        head++;
        size--;

        return item;
    }

    /*public int getMaxSize() {
        return max_size;
    }*/
}