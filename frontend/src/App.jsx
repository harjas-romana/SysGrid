import React, { useState, useEffect, useCallback } from 'react';
import axios from 'axios';
import { 
  ReactFlow, 
  Controls, 
  Background, 
  useNodesState, 
  useEdgesState, 
  addEdge 
} from '@xyflow/react';

const API_BASE = 'http://localhost:8080/api';
const NODE_CLASSIFICATIONS = ['CLIENT', 'LOAD_BALANCER', 'DATABASE', 'CACHE', 'APP_SERVER'];

// Soft, warm color styling for status indicators
const getStatusAesthetic = (status) => {
  switch(status) {
    case 'HEALTHY': return 'text-[#3b5e4d] border-[#3b5e4d] bg-[#f2f8f5]';
    case 'OVERLOADED': return 'text-[#8a3c3c] border-[#8a3c3c] bg-[#faf0f0]';
    case 'WARNING': return 'text-[#9c7a38] border-[#9c7a38] bg-[#fbf7f0]';
    case 'OFFLINE': return 'text-[#706d69] border-[#706d69] bg-[#f5f5f5]';
    default: return 'text-[#706d69] border-[#e4dfd7] bg-[#faf9f6]';
  }
};

function App() {
  const [nodes, setNodes, onNodesChange] = useNodesState([]);
  const [edges, setEdges, onEdgesChange] = useEdgesState([]);
  const [rawNodesList, setRawNodesList] = useState([]); // Keeps a fallback list
  const [stats, setStats] = useState(null);
  const [newNode, setNewNode] = useState({ name: '', type: 'APP_SERVER', maxRps: 100 });

  useEffect(() => {
    fetchRegistryAndBuildMap();
  }, []);

  // Fetch from your Spring Boot backend and construct the interactive node-link map
  const fetchRegistryAndBuildMap = async () => {
    try {
      const [graphRes, statsRes] = await Promise.all([
        axios.get(`${API_BASE}/graph`),
        axios.get(`${API_BASE}/graph/stats`)
      ]);

      const fetchedNodes = graphRes.data.nodes || [];
      const fetchedEdges = graphRes.data.edges || [];
      
      setRawNodesList(fetchedNodes);
      setStats(statsRes.data || {});

      // Calculate simple positions in a circle/grid so they don't spawn on top of each other
      const mappedNodes = fetchedNodes.map((n, idx) => ({
        id: n.id,
        // Visual positioning grid
        position: { x: 150 + (idx % 3) * 220, y: 100 + Math.floor(idx / 3) * 150 },
        data: { 
          label: (
            <div className="text-left font-serif p-1">
              <div className="font-bold text-xs text-[#2c2825]">{n.name}</div>
              <div className="text-[8px] text-[#8c8275] tracking-wider mt-0.5">{n.type}</div>
              <div className="text-[9px] mt-1 font-sans text-right text-indigo-800">{n.currentRps}/{n.maxRps} RPS</div>
            </div>
          )
        },
        style: { 
          background: '#ffffff', 
          border: '1px solid #e4dfd7', 
          borderRadius: '2px',
          boxShadow: '0 4px 12px -5px rgba(0,0,0,0.05)',
          width: 180 
        }
      }));

      const mappedEdges = fetchedEdges.map(e => ({
        id: e.id,
        source: e.sourceId,
        target: e.targetId,
        animated: true,
        style: { stroke: '#a39788', strokeWidth: 1.5 },
      }));

      setNodes(mappedNodes);
      setEdges(mappedEdges);
    } catch (err) {
      console.error("Failed to sync structural state:", err);
    }
  };

  // Triggered when dragging a line to link two existing nodes visually
  const onConnect = useCallback(async (connection) => {
    try {
      // POST the dynamic link to your backend!
      await axios.post(`${API_BASE}/edges`, {
        sourceId: connection.source,
        targetId: connection.target
      });
      // Refresh state to confirm connection registration
      fetchRegistryAndBuildMap();
    } catch (err) {
      alert("Failed to wire connection: " + (err.response?.data?.message || err.message));
    }
  }, [setEdges]);

  const handleCommissionEntity = async (e) => {
    e.preventDefault();
    try {
      await axios.post(`${API_BASE}/nodes`, newNode);
      fetchRegistryAndBuildMap(); 
      setNewNode({ name: '', type: 'APP_SERVER', maxRps: 100 });
    } catch (err) {
      alert("Registration Failed: " + (err.response?.data?.message || err.message));
    }
  };

  return (
    <div className="min-h-screen bg-[#fdfbf7] p-6 md:p-12 font-serif text-[#2c2825] selection:bg-[#d9d0c1] flex flex-col gap-8">
      
      {/* Royal Header Stamp */}
      <header className="border-b border-[#e4dfd7] pb-6 flex justify-between items-end">
        <div>
          <h1 className="text-3xl md:text-4xl font-normal tracking-wide text-[#1a1714]">
            The SysGrid Ledger
          </h1>
          <p className="text-[#7a736a] mt-2 italic text-sm md:text-base">
            An interactive topological workspace for distributed configurations.
          </p>
        </div>
        <div className="text-right hidden md:block">
          <p className="text-[10px] uppercase tracking-widest text-[#938a7f] mb-0.5">Architected By</p>
          <p className="text-sm font-semibold tracking-wider text-[#3d3732]">HARJAS SINGH</p>
          <p className="text-[9px] tracking-widest text-[#938a7f] mt-1 border-t border-[#e4dfd7] pt-1">PROTOTYPE EDITION</p>
        </div>
      </header>

      {/* Dynamic System Performance Ledger */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        {stats ? (
          Object.entries(stats).map(([key, value]) => {
            const displayValue = typeof value === 'object' && value !== null 
              ? JSON.stringify(value) === '{}' ? '0' : 'Data Object' 
              : String(value);

            return (
              <div key={key} className="bg-white p-5 border border-[#e4dfd7] shadow-[0_4px_15px_-10px_rgba(0,0,0,0.02)] rounded-sm">
                <span className="text-[10px] font-semibold text-[#8c8275] uppercase tracking-widest">
                  {key.replace(/([A-Z])/g, ' $1').trim()}
                </span>
                <div className="text-3xl font-light text-[#2c2825] mt-2 font-sans tracking-tight">{displayValue}</div>
              </div>
            );
          })
        ) : (
          <div className="col-span-full text-[#938a7f] italic text-xs">Accessing telemetry matrix...</div>
        )}
      </div>

      {/* Main Workspace Workspace Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-4 gap-8 flex-1">
        
        {/* Provision Control Column */}
        <div className="lg:col-span-1 space-y-6">
          <section className="bg-white p-6 border border-[#e4dfd7] shadow-[0_4px_20px_-10px_rgba(0,0,0,0.03)] rounded-sm">
            <h2 className="text-lg font-normal tracking-wide mb-4 text-[#1a1714] border-b border-[#f0ebe1] pb-2">
              Commission Entity
            </h2>
            <form onSubmit={handleCommissionEntity} className="space-y-4 flex flex-col font-sans">
              <div>
                <label className="block text-[9px] uppercase tracking-widest text-[#8c8275] mb-1 font-serif">Entity Designation</label>
                <input 
                  className="w-full bg-[#fdfbf7] border border-[#e4dfd7] p-2.5 text-xs focus:outline-none focus:border-[#a39788] rounded-sm text-[#2c2825]" 
                  placeholder="e.g., Worker-A" 
                  value={newNode.name} 
                  onChange={e => setNewNode({...newNode, name: e.target.value})} 
                  required 
                />
              </div>
              
              <div>
                <label className="block text-[9px] uppercase tracking-widest text-[#8c8275] mb-1 font-serif">Classification</label>
                <select 
                  className="w-full bg-[#fdfbf7] border border-[#e4dfd7] p-2.5 text-xs focus:outline-none focus:border-[#a39788] rounded-sm text-[#2c2825]"
                  value={newNode.type}
                  onChange={e => setNewNode({...newNode, type: e.target.value})}
                >
                  {NODE_CLASSIFICATIONS.map(type => (
                    <option key={type} value={type}>{type.replace('_', ' ')}</option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-[9px] uppercase tracking-widest text-[#8c8275] mb-1 font-serif">Capacity Limit (RPS)</label>
                <input 
                  className="w-full bg-[#fdfbf7] border border-[#e4dfd7] p-2.5 text-xs focus:outline-none focus:border-[#a39788] rounded-sm text-[#2c2825]" 
                  type="number" 
                  value={newNode.maxRps} 
                  onChange={e => setNewNode({...newNode, maxRps: parseInt(e.target.value)})} 
                />
              </div>

              <button className="mt-2 bg-[#2c2825] text-[#fdfbf7] font-serif uppercase tracking-widest text-[10px] py-3 hover:bg-[#1a1714] transition-colors rounded-sm border border-[#1a1714]">
                Establish Node
              </button>
            </form>
          </section>

          {/* Fallback Directory List */}
          <section className="bg-white p-6 border border-[#e4dfd7] shadow-[0_4px_20px_-10px_rgba(0,0,0,0.03)] rounded-sm max-h-[300px] overflow-y-auto">
            <h2 className="text-sm font-semibold tracking-wider mb-3 text-[#1a1714] uppercase text-[#8c8275]">Local Directory</h2>
            <div className="space-y-2">
              {rawNodesList.map(n => (
                <div key={n.id} className={`p-2.5 border rounded-sm flex justify-between items-center ${getStatusAesthetic(n.status)}`}>
                  <div className="text-xs truncate max-w-[120px] font-sans font-semibold">{n.name}</div>
                  <span className="text-[8px] tracking-wider uppercase font-sans font-bold">{n.status}</span>
                </div>
              ))}
            </div>
          </section>
        </div>

        {/* Central Map Canvas (Takes remaining 3 columns) */}
        <div className="lg:col-span-3 border border-[#e4dfd7] h-[550px] bg-white relative rounded-sm shadow-[inset_0_2px_10px_rgba(0,0,0,0.02)]">
          <div className="absolute top-4 left-4 z-10 pointer-events-none">
            <p className="text-[10px] uppercase tracking-widest text-[#938a7f] bg-white px-2 py-1 border border-[#e4dfd7]">Interactive Topology Desk</p>
          </div>
          
          <ReactFlow
            nodes={nodes}
            edges={edges}
            onNodesChange={onNodesChange}
            onEdgesChange={onEdgesChange}
            onConnect={onConnect}
            fitView
          >
            <Background color="#eae3d9" gap={16} size={1} />
            <Controls className="bg-white border border-[#e4dfd7] shadow-none rounded-none text-[#2c2825] !button:hover:bg-[#fdfbf7]" />
          </ReactFlow>
        </div>

      </div>

      <footer className="border-t border-[#e4dfd7] pt-4 text-center">
        <p className="text-[9px] uppercase tracking-widest text-[#938a7f]">SysGrid Architectural Prototype v1.0 • Designed by Harjas Singh</p>
      </footer>
    </div>
  );
}

export default App;