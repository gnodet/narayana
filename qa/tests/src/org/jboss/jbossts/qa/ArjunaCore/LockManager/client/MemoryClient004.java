/*
 * JBoss, Home of Professional Open Source
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * (C) 2005-2006,
 * @author JBoss Inc.
 */
package org.jboss.jbossts.qa.ArjunaCore.LockManager.client;

import org.jboss.jbossts.qa.ArjunaCore.LockManager.impl.TXBasicLockRecord;
import org.jboss.jbossts.qa.ArjunaCore.Utils.BaseTestClient;
import org.jboss.jbossts.qa.ArjunaCore.Utils.qautil;

public class MemoryClient004 extends BaseTestClient
{
	public static void main(String[] args)
	{
		MemoryClient004 test = new MemoryClient004(args);
	}

	private MemoryClient004(String[] args)
	{
		super(args);
	}

	public void Test()
	{
		try
		{
			setNumberOfCalls(3);
			setNumberOfResources(2);
			getClientThreshold(1);

			TXBasicLockRecord[] mLockRecordList = new TXBasicLockRecord[mNumberOfResources];

			startStopWatch();

			//set up abstract records
			for (int i = 0; i < mNumberOfResources; i++)
			{
				mLockRecordList[i] = new TXBasicLockRecord();
			}

			long stopWatchTime = stopStopWatch();
			System.err.println("Time taken to create records: " + stopWatchTime + "ms");

			for (int j = 0; j < mNumberOfResources; j++)
			{
				startStopWatch();
				for (int i = 0; i < mMaxIteration; i++)
				{
					//start transaction
					startTx();
					mLockRecordList[j].increase();
					if (i % 2 == 0)
					{
						commit();
					}
					else
					{
						abort();
					}
				}
				stopWatchTime = stopStopWatch();
				System.err.println("Time taken to increase resource " + mMaxIteration + " iteration(s): " + stopWatchTime + "ms");
			}

			//get first memory reading.
			getFirstReading();

			for (int j = 0; j < mNumberOfResources; j++)
			{
				startStopWatch();
				for (int i = 0; i < mMaxIteration; i++)
				{
					startTx();
					mLockRecordList[j].increase();
					if (i % 2 == 0)
					{
						commit();
					}
					else
					{
						abort();
					}
				}
				stopWatchTime = stopStopWatch();
				System.err.println("Time taken to increase resource " + mMaxIteration + " iteration(s): " + stopWatchTime + "ms");
			}

			getSecondReading();

			qaMemoryAssert();
		}
		catch (Exception e)
		{
			Fail("Error in MemoryClient004.test() :", e);
		}
	}

}
