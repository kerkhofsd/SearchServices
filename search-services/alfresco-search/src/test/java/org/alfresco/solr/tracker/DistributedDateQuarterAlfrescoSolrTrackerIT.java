/*
 * #%L
 * Alfresco Search Services
 * %%
 * Copyright (C) 2005 - 2020 Alfresco Software Limited
 * %%
 * This file is part of the Alfresco software. 
 * If the software was purchased under a paid Alfresco license, the terms of 
 * the paid license agreement will prevail.  Otherwise, the software is 
 * provided under the following open source license terms:
 * 
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

package org.alfresco.solr.tracker;

import org.alfresco.repo.index.shard.ShardMethodEnum;
import org.apache.solr.SolrTestCaseJ4;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.Properties;

/**
 * Tests sharding by quarter
 * @author Gethin James
 */

@SolrTestCaseJ4.SuppressSSL
public class DistributedDateQuarterAlfrescoSolrTrackerIT extends DistributedDateAbstractSolrTrackerIT
{
    @BeforeClass
    public static void initData() throws Throwable
    {
        initSolrServers(6, getSimpleClassName(), getShardMethod());
    }

    @AfterClass
    public static void destroyData()
    {
        dismissSolrServers();
    }
    
    @Override
    protected void assertCorrect(int numNodes) throws Exception
    {
        //We should expect roughly 25% on each of the 4 cores
        int shardHits = assertNodesPerShardGreaterThan((int)((numNodes)*.22), true);
        //We have 6 shards but we are sharding by quarter so only 4 of the shards should be used.
        assertEquals(4, shardHits);
    }

    protected static Properties getShardMethod()
    {
        Properties prop = new Properties();
        prop.put("shard.method", ShardMethodEnum.DATE.toString());
        prop.put("shard.date.grouping", "3");
        return prop;
    }
}

