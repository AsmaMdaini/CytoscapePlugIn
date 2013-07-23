package org.reactome.cytoscape3;

/**
 * This class creates the necessary CyTable columns
 * in the default tables and formats their homologs
 * across node, edge, and network tables if needed.
 * It also stores static fields for certain CyTable
 * value/column names and creates custom tables for
 * various app functions.
 * @author Eric T Dawson
 * @date July 2013 
 */
import javax.swing.JOptionPane;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableFactory;
import org.cytoscape.model.CyTableManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class TableFormatter
{

    //Strings important to multiple classes in the package are cached here.
    private static final String FI_NETWORK_VERSION = "Reactome_FI_Network_Version";
    private static final String SAMPLE_MUTATION_DATA = "SAMPLE_MUTATION_DATA";
    private static final String MCL_ARRAY_DATA = "MCL_ARRAY_DATA";
    private static final String MCL_ARRAY_CLUSTERING = "MCL_ARRAY_CLUSTERING";
    private static final String HOTNET_MODULE = "HOTNET_MODULE";
    private static final String SPECTRAL_PARTITION_CLUSTER = "SPECTRAL_PARTITION_CLUSTERING";
    private CyTableFactory tableFactory;
    private CyTableManager tableManager;

    // private final String [] COLUMNS = new String [7];
    // COLUMNS = ["network", "isReactomeFINetwork", FI_NETWORK_VERSION,
    // "DataSetType", "moduleToSampleValue", "Clustering_Type", "isLinker"];

    public TableFormatter(CyTableFactory tableFactory, CyTableManager tableManager)
    {
        this.tableFactory = tableFactory;
        this.tableManager = tableManager;
    }

    public static String getFINetworkVersion()
    {
        return FI_NETWORK_VERSION;
    }

    public static String getSampleMutationData()
    {
        return SAMPLE_MUTATION_DATA;
    }

    public static String getMCLArrayClustering()
    {
        return MCL_ARRAY_DATA;
    }

    public static String getSpectralPartitionCluster()
    {
        return SPECTRAL_PARTITION_CLUSTER;
    }

    public void makeGeneSetMutationAnalysisTables(CyNetwork network)
    {
        //Grab default tables from the network.
        CyTable netTable = network.getDefaultNetworkTable();
        CyTable nodeTable = network.getDefaultNodeTable();
        CyTable edgeTable = network.getDefaultEdgeTable();

        // Creates a set of columns in the default network table and creates
        // matching virtual columns
        // within the default edge and node tables upon network creation or
        // network view creation.
        if (netTable.getColumn("isReactomeFINetwork") == null)
        {
            netTable.createColumn("isReactomeFINetwork", Boolean.class,
                    Boolean.FALSE);
        }
        if (netTable.getColumn(FI_NETWORK_VERSION) == null)
        {
            netTable.createColumn(FI_NETWORK_VERSION, String.class,
                    Boolean.FALSE);
        }
        if (netTable.getColumn("dataSetType") == null)
        {
            netTable.createColumn("dataSetType", String.class, Boolean.FALSE);
        }
        if (nodeTable.getColumn("module") == null)
        {
            nodeTable.createColumn("module", Integer.class, Boolean.FALSE);
        }
        if (netTable.getColumn("clustering_Type") == null)
        {
            netTable.createColumn("clustering_Type", String.class,
                    Boolean.FALSE);
        }
        if (nodeTable.getColumn("samples") == null)
        {
            nodeTable.createColumn("samples", String.class, Boolean.FALSE);
        }

        if (nodeTable.getColumn("isLinker") == null)
        {
            nodeTable.createColumn("isLinker", Boolean.class, Boolean.FALSE);
        }
        if (nodeTable.getColumn("nodeLabel") == null)
        {
            nodeTable.createColumn("nodeLabel", String.class, Boolean.FALSE);
        }
        if (nodeTable.getColumn("sampleNumber") == null)
        {
            nodeTable
                    .createColumn("sampleNumber", Integer.class, Boolean.FALSE);
        }
        if (nodeTable.getColumn("commonName") == null)
        {
            nodeTable.createColumn("commonName", String.class, Boolean.FALSE);
        }
        if (nodeTable.getColumn("nodeToolTip") == null)
        {
            nodeTable.createColumn("nodeToolTip", String.class, Boolean.FALSE);
        }
        if (edgeTable.getColumn("name") == null)
        {
            edgeTable.createColumn("name", String.class, Boolean.FALSE);
        }

    }

    public void makeModuleAnalysisTables(CyNetwork network)
    {
        //Creates the attributes tables for Module Analysis (view context menu)
        CyTable netTable = network.getDefaultNetworkTable();
        CyTable nodeTable = network.getDefaultNodeTable();
        if (netTable.getColumn("clustering_Type") == null)
        {
            netTable.createColumn("clustering_Type", Integer.class,
                    Boolean.FALSE);
        }
        if (nodeTable.getColumn("module") == null)
        {
            nodeTable.createColumn("module", Integer.class, Boolean.FALSE);
        }
        CyTable moduleTable = tableFactory.createTable("Network Module",
                "module", Integer.class, Boolean.TRUE, Boolean.FALSE);
        if (moduleTable.getColumn("Nodes in Module") == null)
        {
            moduleTable.createColumn("Nodes in Module", Integer.class,
                    Boolean.FALSE);
        }
        if (moduleTable.getColumn("Node Percentage") == null)
        {
            moduleTable.createColumn("Node Percentage", Integer.class,
                    Boolean.FALSE);
        }
        if (moduleTable.getColumn("Samples in Module") == null)
        {
            moduleTable.createColumn("Samples in Module", Integer.class, Boolean.FALSE);
        }
        if (moduleTable.getColumn("Sample Percentage") == null)
        {
            moduleTable.createColumn("Sample Percentage", Double.class, Boolean.FALSE);
        }
        if (moduleTable.getColumn("Node List") == null)
        {
            moduleTable.createColumn("Node List", String.class, Boolean.FALSE);
        }
    }
    public void makeHotNetAnalysisTables(CyNetwork network)
    {
        //Creates the attribute tables for HotNet Analysis
        CyTable nodeTable = network.getDefaultNodeTable();
        CyTable hotNetTable = tableFactory.createTable("HotNet Module", "module", Integer.class, Boolean.TRUE, Boolean.FALSE);
        if (nodeTable.getColumn("module") == null)
        {
            nodeTable.createColumn("module", Integer.class, Boolean.FALSE);
        }
        if (hotNetTable.getColumn("module") == null)
        {
            hotNetTable.createColumn("module", Integer.class, Boolean.FALSE);
        }
        if (hotNetTable.getColumn("Nodes in Module") == null)
        {
            hotNetTable.createColumn("Nodes in Module", Integer.class, Boolean.FALSE);
        }
        if (hotNetTable.getColumn("Node Percentage") == null)
        {
            hotNetTable.createColumn("Node Percentage", Integer.class,
                    Boolean.FALSE);
        }
        if (hotNetTable.getColumn("Samples in Module") == null)
        {
            hotNetTable.createColumn("Samples in Module", Integer.class, Boolean.FALSE);
        }
        if (hotNetTable.getColumn("Sample Percentage") == null)
        {
            hotNetTable.createColumn("Sample Percentage", Double.class, Boolean.FALSE);
        }
        if (hotNetTable.getColumn("pvalue") == null)
        {
            hotNetTable.createColumn("pvalue", Double.class, Boolean.FALSE);
        }
        if (hotNetTable.getColumn("FDR") == null)
        {
            hotNetTable.createColumn("FDR", Double.class, Boolean.FALSE);
        }
        if (hotNetTable.getColumn("Node List") == null)
        {
            hotNetTable.createColumn("Node List", String.class, Boolean.FALSE);
        }
        tableManager.addTable(hotNetTable);
    }
    public void makeEnrichmentTables(CyNetwork network, CyTable table)
    {

        if (table.getColumn("Gene Set") == null)
        {
            table.createColumn("Gene Set", String.class, Boolean.FALSE);
        }
        if (table.getColumn("Ratio of Protein in Gene Set") == null)
        {
            table.createColumn("Ratio of Protein in Gene Set", Double.class, Boolean.FALSE);
        }
        if (table.getColumn("Number of Protein in Gene Set") == null)
        {
            table.createColumn("Number of Protein in Gene Set", Integer.class, Boolean.FALSE);
        }
        if (table.getColumn("Protein from Network") == null)
        {
            table.createColumn("Protein from Network", Integer.class, Boolean.FALSE);
        }
        if (table.getColumn("P-Value") == null)
        {
            table.createColumn("P-Value", Double.class, Boolean.FALSE);
        }
        if (table.getColumn("FDR") == null)
        {
            table.createColumn("FDR", Double.class, Boolean.FALSE);
        }
        if (table.getColumn("Nodes") == null)
        {
            table.createColumn("Nodes", String.class, Boolean.FALSE);
        }
    }
    public void makeNetPathEnrichmentTables(CyNetwork network)
    {
        CyTable netPathEnrichmentTable = tableFactory.createTable("Pathways in Network", "Gene Set", String.class, Boolean.TRUE, Boolean.FALSE);
        makeEnrichmentTables(network, netPathEnrichmentTable);
        tableManager.addTable(netPathEnrichmentTable);
    }
    public void makeNetCellComponentTables(CyNetwork network)
    {
       CyTable netCellComponentTable = tableFactory.createTable("GO CC in Network", "Gene Set", String.class, Boolean.TRUE, Boolean.FALSE);
       makeEnrichmentTables(network, netCellComponentTable);
       tableManager.addTable(netCellComponentTable);
    }
    public void makeNetBiologicalProcessTable(CyNetwork network)
    {
        CyTable netBiologicalProcessTable = tableFactory.createTable("GO BP in Network", "Gene Set", String.class, Boolean.TRUE, Boolean.FALSE);
        makeEnrichmentTables(network, netBiologicalProcessTable);
        tableManager.addTable(netBiologicalProcessTable);
    }
    public void makeNetMolecularFunctionTables(CyNetwork network)
    {
        CyTable netMolecularFunctionTable = tableFactory.createTable("GO MF in Network", "Gene Set", String.class, Boolean.TRUE, Boolean.FALSE);
        netMolecularFunctionTable.setTitle("GO MF in Network");
        makeEnrichmentTables(network, netMolecularFunctionTable);
        tableManager.addTable(netMolecularFunctionTable);
    }
}