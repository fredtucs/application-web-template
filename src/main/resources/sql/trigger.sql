CREATE OR REPLACE TRIGGER comprobante_pago_before_insert
BEFORE INSERT OR UPDATE ON t_comprobantepago
FOR EACH ROW
WHEN (new.codigo IS NULL)
  DECLARE
    v_codigo VARCHAR2(50);
    v_idexp NUMBER(19);
  BEGIN
    v_idexp := :new.idexpedientepago;
    SELECT lpad(nvl(max(cast(cp.codigo AS NUMBER(10, 0))), 0) + 1, 5, '0')
    INTO v_codigo
    FROM t_comprobantepago cp
      LEFT JOIN t_expedientepago ep
        ON ep.idexpedientepago = cp.idexpedientepago
    WHERE cp.estado <> -1
          AND ep.idtipocuenta = (SELECT tc.idtipocuenta
                                 FROM t_expedientepago e
                                   LEFT JOIN t_tipocuenta tc
                                     ON tc.idtipocuenta = e.idtipocuenta
                                 WHERE e.idexpedientepago = v_idexp)
          AND ep.idperiodo = (SELECT p.idperiodo
                              FROM t_expedientepago e
                                LEFT JOIN t_periodos p
                                  ON e.idperiodo = p.idperiodo
                              WHERE e.idexpedientepago = v_idexp);
    :new.codigo := v_codigo;
  END;

CREATE OR REPLACE TRIGGER comp_pago_cheque_before_insert
BEFORE INSERT OR UPDATE ON t_comprobantepago
FOR EACH ROW
WHEN (new.NRODOCUMENTO IS NULL AND new.TIPODOCUMENTO = 1)
  DECLARE
    v_nrodocument VARCHAR2(50);
    v_idexp NUMBER(19);
  BEGIN
    v_idexp := :new.idexpedientepago;
    SELECT lpad(nvl(max(cast(cp.NRODOCUMENTO AS NUMBER(10, 0))), 0) + 1, 8, '0')
    INTO v_nrodocument
    FROM t_comprobantepago cp
      LEFT JOIN t_expedientepago ep
        ON ep.idexpedientepago = cp.idexpedientepago
    WHERE cp.estado <> -1
          AND ep.idtipocuenta = (SELECT tc.idtipocuenta
                                 FROM t_expedientepago e
                                   LEFT JOIN t_tipocuenta tc
                                     ON tc.idtipocuenta = e.idtipocuenta
                                 WHERE e.idexpedientepago = v_idexp)
          AND ep.idperiodo = (SELECT p.idperiodo
                              FROM t_expedientepago e
                                LEFT JOIN t_periodos p
                                  ON e.idperiodo = p.idperiodo
                              WHERE e.idexpedientepago = v_idexp);
    :new.NRODOCUMENTO := v_nrodocument;
  END;