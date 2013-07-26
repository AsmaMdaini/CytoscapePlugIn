package org.reactome.cytoscape3;

/**
 * This class generates a network based upon
 * FI interactions from a given input file
 * and the Reactome database.
 * @author Eric T Dawson
 */
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableFactory;
import org.cytoscape.model.CyTableManager;
import org.cytoscape.work.TaskManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class FINetworkGenerator
{
    private CyNetworkFactory networkFactory;
    private CyTableFactory tableFactory;
    private CyTableManager tableManager;
    private ServiceReference tableFormatterServRef;
    private TableFormatterImpl tableFormatter;
    private ServiceReference networkFactoryRef;
    private ServiceReference tableManagerRef;
    private ServiceReference tableFactoryRef;

    public FINetworkGenerator(CyNetworkFactory networkFactory,
            CyTableFactory tableFactory, CyTableManager tableManager)
    {
        this.networkFactory = networkFactory;
        this.tableFactory = tableFactory;
        this.tableManager = tableManager;
    }
    public FINetworkGenerator()
    {
       // getCyServices();
    }
    private void getTableFormatter()
    {
        try
        {
            BundleContext context = PlugInScopeObjectManager.getManager().getBundleContext();
            ServiceReference servRef = context.getServiceReference(TableFormatter.class.getName());
            if (servRef != null)
            {
                this.tableFormatterServRef = servRef;
                this.tableFormatter = (TableFormatterImpl) context.getService(servRef);
            }
            else
                throw new Exception();
        }
        catch (Throwable t)
        {
            JOptionPane.showMessageDialog(null, "The table formatter could not be retrieved.", "Table Formatting Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    private void releaseTableFormatter()
    {
        BundleContext context = PlugInScopeObjectManager.getManager().getBundleContext();
        context.ungetService(tableFormatterServRef);
    }
    private void getCyServices()
    {
        Map<ServiceReference,  Object> netFactoryRefToObj =  PlugInScopeObjectManager.getManager().getServiceReferenceObject(CyNetworkFactory.class.getName());
        ServiceReference servRef = (ServiceReference) netFactoryRefToObj.keySet().toArray()[0];
        CyNetworkFactory netFactory = (CyNetworkFactory) netFactoryRefToObj.get(servRef);
        this.networkFactory = netFactory;
        this.networkFactoryRef = servRef;
        
        Map<ServiceReference,  Object> tableManagerRefToObj =  PlugInScopeObjectManager.getManager().getServiceReferenceObject(CyTableManager.class.getName());
        servRef = (ServiceReference) tableManagerRefToObj.keySet().toArray()[0];
        CyTableManager tableManager = (CyTableManager) tableManagerRefToObj.get(servRef);
        this.tableManager = tableManager;
        this.tableManagerRef = servRef;
        
        Map<ServiceReference,  Object> tableFactoryRefToObj =  PlugInScopeObjectManager.getManager().getServiceReferenceObject(CyTableFactory.class.getName());
        servRef = (ServiceReference) tableFactoryRefToObj.keySet().toArray()[0];
        CyTableFactory tableFactory = (CyTableFactory) tableFactoryRefToObj.get(servRef);
        this.tableFactory = tableFactory;
        this.tableFactoryRef = servRef;
        
        getTableFormatter();
    }
    
    private void releaseCyServices()
    {
        BundleContext context = PlugInScopeObjectManager.getManager().getBundleContext();
        if (networkFactoryRef != null)
            context.ungetService(networkFactoryRef);
        if (tableManagerRef != null)
            context.ungetService(tableManagerRef);
        if (tableFactoryRef != null)
            context.ungetService(tableFactoryRef);
        if (tableFormatterServRef != null)
            releaseTableFormatter();
    }
    public CyNetwork constructFINetwork(Set<String> nodes, Set<String> fis)
    {
        getCyServices();
        // Construct an empty network.
        CyNetwork network = networkFactory.createNetwork();
        tableFormatter.makeGeneSetMutationAnalysisTables(network);
        // Generate a source, edge and target for each FI interaction
        // retrieved from the Reactome database.
        int index = 0;
        Map<String, CyNode> name2Node = new HashMap<String, CyNode>();
        for (String fi : fis)
        {
            index = fi.indexOf("\t");
            String name1 = fi.substring(0, index);
            String name2 = fi.substring(index + 1);
            CyNode node1 = getNode(name1, name2Node, network);
            CyNode node2 = getNode(name2, name2Node, network);
            // CyEdge edge =
            createEdge(network, node1, node2, "FI");
        }
        // Put nodes that are not linked to other genes in the network.
        if (nodes != null)
        {
            Set<String> copy = new HashSet<String>(nodes);
            copy.removeAll(name2Node.keySet());
            for (String name : copy)
            {
                CyNode node = getNode(name, name2Node, network);
            }
        }
        releaseCyServices();
        return network;
    }

    private CyEdge createEdge(CyNetwork network, CyNode node1, CyNode node2,
            String type)
    {
        // Add the edge to the network
        CyEdge edge = network.addEdge(node1, node2, true);

        return edge;
    }

    private CyNode getNode(String name, Map<String, CyNode> nameToNode,
            CyNetwork network)
    {
        // Retrieve a node's name from the hashmap.
        // If it exists, return it. Otherwise, create
        // the node.
        CyNode node = nameToNode.get(name);
        if (node != null) return node;
        node = createNode(network, name, "Gene", name);
        CyTable nodeTable = network.getDefaultNodeTable();
        Long nodeSUID = node.getSUID();
        nodeTable.getRow(nodeSUID).set("name", name);
        nameToNode.put(name, node);
        return node;
    }

    private CyNode createNode(CyNetwork network, String label, String type,
            String tooltip)
    {
        CyNode node = network.addNode();
        Long nodeSUID = node.getSUID();
        // Add node labels, tooltips, common names, etc.
        CyTable nodeTable = network.getDefaultNodeTable();
        // nodeTable.getRow(nodeSUID).set("NodeType", type);
        nodeTable.getRow(nodeSUID).set("nodeLabel", label);
        nodeTable.getRow(nodeSUID).set("commonName", label);
        nodeTable.getRow(nodeSUID).set("nodeToolTip", tooltip);
        return node;

    }

    public CyNetwork constructFINetwork(Set<String> fis)
    {
        // This method is just a convenience method which
        // overloads constructFINetwork.
        return constructFINetwork(null, fis);
    }
}
