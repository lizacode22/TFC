const { onSchedule } = require("firebase-functions/v2/scheduler");
const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

const db = admin.firestore();

exports.reiniciarReservasSemanal = onSchedule(
  {
    schedule: "0 0 * * 1", // Lunes a las 00:00
    timeZone: "Europe/Madrid",
  },
  async (event) => {
    try {
      const snapshot = await db.collection("clases").get();
      const batch = db.batch();
      const ahora = admin.firestore.Timestamp.now();

      for (const doc of snapshot.docs) {
        const data = doc.data();

        // Solo guarda en historial si hay reservas
        if ((data.usuarios && data.usuarios.length > 0) || data.inscritos > 0) {
          const historialRef = db.collection("historialClases").doc();
          await historialRef.set({
            claseId: doc.id,
            titulo: data.titulo || "",
            dia: data.dia || "",
            hora: data.hora || "",
            capacidad: data.capacidad || 0,
            usuarios: data.usuarios || [],
            inscritos: data.inscritos || 0,
            timestamp: ahora
          });
        }

        // Reiniciar los campos
        batch.update(doc.ref, {
          usuarios: [],
          inscritos: 0
        });
      }

      await batch.commit();
      console.log("Reservas reiniciadas y archivadas correctamente.");
    } catch (error) {
      console.error("Error al reiniciar y archivar reservas:", error);
    }
  }
);