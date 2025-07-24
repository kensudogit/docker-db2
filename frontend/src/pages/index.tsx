import React from 'react';
import {
  Box,
  Container,
  Typography,
  Grid,
  Card,
  CardContent,
  CardActions,
  Button,
  AppBar,
  Toolbar,
} from '@mui/material';
import {
  Flight,
  LocalShipping,
  Timeline,
  Dashboard,
} from '@mui/icons-material';
import { useRouter } from 'next/router';

export default function Home() {
  const router = useRouter();

  const menuItems = [
    {
      title: '貨物管理',
      description: '貨物の登録、編集、削除を行います',
      icon: <Flight sx={{ fontSize: 40 }} />,
      path: '/cargo',
      color: '#1976d2',
    },
    {
      title: '入荷管理',
      description: '入荷の登録とステータス管理を行います',
      icon: <LocalShipping sx={{ fontSize: 40 }} />,
      path: '/inbound',
      color: '#2e7d32',
    },
    {
      title: '出荷管理',
      description: '出荷の登録とステータス管理を行います',
      icon: <LocalShipping sx={{ fontSize: 40 }} />,
      path: '/outbound',
      color: '#ed6c02',
    },
    {
      title: '追跡管理',
      description: '貨物の追跡情報を管理します',
      icon: <Timeline sx={{ fontSize: 40 }} />,
      path: '/tracking',
      color: '#9c27b0',
    },
    {
      title: 'ダッシュボード',
      description: 'システム全体の統計情報を表示します',
      icon: <Dashboard sx={{ fontSize: 40 }} />,
      path: '/dashboard',
      color: '#d32f2f',
    },
  ];

  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar>
          <Flight sx={{ mr: 2 }} />
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            航空貨物ロジステックシステム
          </Typography>
        </Toolbar>
      </AppBar>

      <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom align="center">
          航空貨物ロジステックシステム
        </Typography>
        <Typography variant="h6" component="h2" gutterBottom align="center" color="text.secondary">
          貨物の入出荷管理と追跡機能を提供するシステムです
        </Typography>

        <Grid container spacing={3} sx={{ mt: 4 }}>
          {menuItems.map((item, index) => (
            <Grid item xs={12} sm={6} md={4} key={index}>
              <Card
                sx={{
                  height: '100%',
                  display: 'flex',
                  flexDirection: 'column',
                  '&:hover': {
                    transform: 'translateY(-4px)',
                    transition: 'transform 0.2s ease-in-out',
                    boxShadow: 3,
                  },
                }}
              >
                <CardContent sx={{ flexGrow: 1, textAlign: 'center' }}>
                  <Box sx={{ color: item.color, mb: 2 }}>
                    {item.icon}
                  </Box>
                  <Typography gutterBottom variant="h5" component="h2">
                    {item.title}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {item.description}
                  </Typography>
                </CardContent>
                <CardActions>
                  <Button
                    size="small"
                    fullWidth
                    onClick={() => router.push(item.path)}
                    sx={{ color: item.color }}
                  >
                    アクセス
                  </Button>
                </CardActions>
              </Card>
            </Grid>
          ))}
        </Grid>

        <Box sx={{ mt: 6, textAlign: 'center' }}>
          <Typography variant="body2" color="text.secondary">
            航空貨物ロジステックシステム v1.0.0
          </Typography>
        </Box>
      </Container>
    </Box>
  );
} 