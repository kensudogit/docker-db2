import React, { useState, useEffect } from 'react';
import {
  Box,
  Container,
  Typography,
  Button,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  MenuItem,
  AppBar,
  Toolbar,
  IconButton,
} from '@mui/material';
import { Add, Edit, Delete, Search, ArrowBack } from '@mui/icons-material';
import { useRouter } from 'next/router';

interface Cargo {
  cargoId: string;
  flightNumber: string;
  originAirport: string;
  destinationAirport: string;
  cargoType: string;
  weight: number;
  volume: number;
  status: string;
  shipperName: string;
  consigneeName: string;
  createdDate: string;
  updatedDate: string;
}

export default function CargoPage() {
  const router = useRouter();
  const [cargos, setCargos] = useState<Cargo[]>([]);
  const [openDialog, setOpenDialog] = useState(false);
  const [editingCargo, setEditingCargo] = useState<Cargo | null>(null);
  const [searchTerm, setSearchTerm] = useState('');

  const [formData, setFormData] = useState({
    flightNumber: '',
    originAirport: '',
    destinationAirport: '',
    cargoType: '',
    weight: '',
    volume: '',
    shipperName: '',
    consigneeName: '',
  });

  const cargoTypes = [
    '一般貨物',
    '冷蔵貨物',
    '危険物',
    '生鮮食品',
    '医薬品',
    '電子機器',
    '衣類',
    'その他',
  ];

  const statuses = [
    'PENDING',
    'IN_TRANSIT',
    'ARRIVED',
    'DELIVERED',
    'CANCELLED',
  ];

  const airports = [
    'NRT', 'HND', 'LAX', 'JFK', 'LHR', 'CDG',
  ];

  useEffect(() => {
    // 実際のAPI呼び出しに置き換える
    fetchCargos();
  }, []);

  const fetchCargos = async () => {
    try {
      const response = await fetch('/api/cargo');
      if (response.ok) {
        const data = await response.json();
        setCargos(data);
      }
    } catch (error) {
      console.error('貨物データの取得に失敗しました:', error);
    }
  };

  const handleSubmit = async () => {
    try {
      const url = editingCargo 
        ? `/api/cargo/${editingCargo.cargoId}`
        : '/api/cargo';
      
      const method = editingCargo ? 'PUT' : 'POST';
      
      const response = await fetch(url, {
        method,
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          ...formData,
          weight: parseFloat(formData.weight),
          volume: parseFloat(formData.volume),
        }),
      });

      if (response.ok) {
        setOpenDialog(false);
        setEditingCargo(null);
        resetForm();
        fetchCargos();
      }
    } catch (error) {
      console.error('貨物の保存に失敗しました:', error);
    }
  };

  const handleEdit = (cargo: Cargo) => {
    setEditingCargo(cargo);
    setFormData({
      flightNumber: cargo.flightNumber,
      originAirport: cargo.originAirport,
      destinationAirport: cargo.destinationAirport,
      cargoType: cargo.cargoType,
      weight: cargo.weight.toString(),
      volume: cargo.volume.toString(),
      shipperName: cargo.shipperName,
      consigneeName: cargo.consigneeName,
    });
    setOpenDialog(true);
  };

  const handleDelete = async (cargoId: string) => {
    if (confirm('この貨物を削除しますか？')) {
      try {
        const response = await fetch(`/api/cargo/${cargoId}`, {
          method: 'DELETE',
        });
        if (response.ok) {
          fetchCargos();
        }
      } catch (error) {
        console.error('貨物の削除に失敗しました:', error);
      }
    }
  };

  const resetForm = () => {
    setFormData({
      flightNumber: '',
      originAirport: '',
      destinationAirport: '',
      cargoType: '',
      weight: '',
      volume: '',
      shipperName: '',
      consigneeName: '',
    });
  };

  const filteredCargos = cargos.filter(cargo =>
    cargo.cargoId.toLowerCase().includes(searchTerm.toLowerCase()) ||
    cargo.flightNumber.toLowerCase().includes(searchTerm.toLowerCase()) ||
    cargo.shipperName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    cargo.consigneeName.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar>
          <IconButton
            edge="start"
            color="inherit"
            onClick={() => router.push('/')}
            sx={{ mr: 2 }}
          >
            <ArrowBack />
          </IconButton>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            貨物管理
          </Typography>
        </Toolbar>
      </AppBar>

      <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3 }}>
          <Typography variant="h4" component="h1">
            貨物一覧
          </Typography>
          <Button
            variant="contained"
            startIcon={<Add />}
            onClick={() => {
              setEditingCargo(null);
              resetForm();
              setOpenDialog(true);
            }}
          >
            新規貨物登録
          </Button>
        </Box>

        <Box sx={{ mb: 3 }}>
          <TextField
            fullWidth
            variant="outlined"
            placeholder="貨物ID、フライト番号、荷送人名、荷受人名で検索..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            InputProps={{
              startAdornment: <Search sx={{ mr: 1, color: 'text.secondary' }} />,
            }}
          />
        </Box>

        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>貨物ID</TableCell>
                <TableCell>フライト番号</TableCell>
                <TableCell>出発空港</TableCell>
                <TableCell>到着空港</TableCell>
                <TableCell>貨物タイプ</TableCell>
                <TableCell>重量(kg)</TableCell>
                <TableCell>容積(m³)</TableCell>
                <TableCell>ステータス</TableCell>
                <TableCell>荷送人</TableCell>
                <TableCell>荷受人</TableCell>
                <TableCell>操作</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {filteredCargos.map((cargo) => (
                <TableRow key={cargo.cargoId}>
                  <TableCell>{cargo.cargoId}</TableCell>
                  <TableCell>{cargo.flightNumber}</TableCell>
                  <TableCell>{cargo.originAirport}</TableCell>
                  <TableCell>{cargo.destinationAirport}</TableCell>
                  <TableCell>{cargo.cargoType}</TableCell>
                  <TableCell>{cargo.weight}</TableCell>
                  <TableCell>{cargo.volume}</TableCell>
                  <TableCell>{cargo.status}</TableCell>
                  <TableCell>{cargo.shipperName}</TableCell>
                  <TableCell>{cargo.consigneeName}</TableCell>
                  <TableCell>
                    <IconButton
                      size="small"
                      onClick={() => handleEdit(cargo)}
                    >
                      <Edit />
                    </IconButton>
                    <IconButton
                      size="small"
                      onClick={() => handleDelete(cargo.cargoId)}
                    >
                      <Delete />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>

        <Dialog open={openDialog} onClose={() => setOpenDialog(false)} maxWidth="md" fullWidth>
          <DialogTitle>
            {editingCargo ? '貨物編集' : '新規貨物登録'}
          </DialogTitle>
          <DialogContent>
            <Box sx={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 2, mt: 2 }}>
              <TextField
                label="フライト番号"
                value={formData.flightNumber}
                onChange={(e) => setFormData({ ...formData, flightNumber: e.target.value })}
                required
              />
              <TextField
                select
                label="出発空港"
                value={formData.originAirport}
                onChange={(e) => setFormData({ ...formData, originAirport: e.target.value })}
                required
              >
                {airports.map((airport) => (
                  <MenuItem key={airport} value={airport}>
                    {airport}
                  </MenuItem>
                ))}
              </TextField>
              <TextField
                select
                label="到着空港"
                value={formData.destinationAirport}
                onChange={(e) => setFormData({ ...formData, destinationAirport: e.target.value })}
                required
              >
                {airports.map((airport) => (
                  <MenuItem key={airport} value={airport}>
                    {airport}
                  </MenuItem>
                ))}
              </TextField>
              <TextField
                select
                label="貨物タイプ"
                value={formData.cargoType}
                onChange={(e) => setFormData({ ...formData, cargoType: e.target.value })}
                required
              >
                {cargoTypes.map((type) => (
                  <MenuItem key={type} value={type}>
                    {type}
                  </MenuItem>
                ))}
              </TextField>
              <TextField
                label="重量(kg)"
                type="number"
                value={formData.weight}
                onChange={(e) => setFormData({ ...formData, weight: e.target.value })}
                required
              />
              <TextField
                label="容積(m³)"
                type="number"
                value={formData.volume}
                onChange={(e) => setFormData({ ...formData, volume: e.target.value })}
                required
              />
              <TextField
                label="荷送人名"
                value={formData.shipperName}
                onChange={(e) => setFormData({ ...formData, shipperName: e.target.value })}
                required
              />
              <TextField
                label="荷受人名"
                value={formData.consigneeName}
                onChange={(e) => setFormData({ ...formData, consigneeName: e.target.value })}
                required
              />
            </Box>
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setOpenDialog(false)}>キャンセル</Button>
            <Button onClick={handleSubmit} variant="contained">
              {editingCargo ? '更新' : '登録'}
            </Button>
          </DialogActions>
        </Dialog>
      </Container>
    </Box>
  );
} 